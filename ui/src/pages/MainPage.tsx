import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, TablePagination,
    Button, Box, Typography, Link, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from "@mui/material";
import axiosInstance from "../axiosConfig";

interface GitHubUser {
  id: number;
  login: string;
  avatarUrl: string;
  htmlUrl: string;
}


const MainPage: React.FC = () => {
  const [users, setUsers] = useState<GitHubUser[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [page, setPage] = useState<number>(0);
  const [rowsPerPage, setRowsPerPage] = useState<number>(10);
  const [totalUsers, setTotalUsers] = useState<number>(0);
  const navigate = useNavigate();

  useEffect(() => {
    fetchUsers(page, rowsPerPage);
  }, [page, rowsPerPage]);

  const fetchUsers = async (page: number, rowsPerPage: number) => {
    setLoading(true);
    try {
      const response = await axiosInstance.get(`/github/users?page=${page}&size=${rowsPerPage}`);
      setUsers(response.data.content);
      setTotalUsers(response.data.totalElements);
    } catch (err) {
      console.error("Failed to fetch users", err);
    } finally {
      setLoading(false);
    }
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

const handleChangeRowsPerPageSelect = (event: SelectChangeEvent<number>) => {
  setRowsPerPage(Number(event.target.value));
  setPage(0);
};

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };


  const handleLogout = async () => {
    try {
      const response = await axiosInstance.post("/auth/logout");
      console.log("Response on logout ", response.status);
      if (response.status === 200){
          localStorage.removeItem("authToken");
          navigate('/auth/login');
      }

    } catch (error) {
      console.error("Error logging out", error);
    }
  };

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        GitHub Users
      </Typography>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <FormControl size="small" sx={{ minWidth: 150 }}>
          <InputLabel id="rows-per-page-label">Rows per page</InputLabel>
          <Select
            labelId="rows-per-page-label"
            value={rowsPerPage}
            onChange={handleChangeRowsPerPageSelect}
          >
            <MenuItem value={10}>10</MenuItem>
            <MenuItem value={15}>15</MenuItem>
            <MenuItem value={20}>20</MenuItem>
            <MenuItem value={25}>25</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" color="secondary" onClick={handleLogout}>
          Logout
        </Button>
      </Box>
      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>
                    <Box display="flex" alignItems="center" gap={2}>
                       <img src={user.avatarUrl}
                            alt={`${user.login}'s avatar`}
                            width={50} height={50}
                             style={{
                                 borderRadius: "50%",
                                 objectFit: "cover",
                             }}/>
                       <Link href={user.htmlUrl} target="_blank" rel="noopener noreferrer" underline="hover">
                         {user.login}
                       </Link>
                    </Box>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[10, 15, 20, 25]}
          component="div"
          count={totalUsers}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Paper>
      {loading && (
        <Typography variant="subtitle1" color="textSecondary" align="center">
          Loading...
        </Typography>
      )}
    </Box>
  );
};

export default MainPage;
