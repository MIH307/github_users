import React from 'react';
import {
  TablePagination,
  TablePaginationProps,
  Box,
  Typography, IconButton
} from '@mui/material';
import { KeyboardArrowLeft, KeyboardArrowRight, FirstPage, LastPage } from '@mui/icons-material';

const CustomTablePagination: React.FC<TablePaginationProps> = (props) => {
  const { count, rowsPerPage, page, onPageChange } = props;
  const isFirstPage = page === 0;
  const isLastPage = (page + 1) * rowsPerPage >= count;

  return (
    <Box display="flex" justifyContent="space-between" alignItems="center" width="100%" p={2}>
      <Typography variant="body2" color="textSecondary">
        {`Showing ${page * rowsPerPage + 1}-${Math.min((page + 1) * rowsPerPage, count)} of ${count}`}
      </Typography>
      <Box display="flex" alignItems="center" gap={1}>
        <Box
                  display="flex"
                  justifyContent="center"
                  alignItems="center"
                  sx={{
                    width: 32,
                    height: 32,
                    borderRadius: "50%",
                    backgroundColor: "black",
                    color: "white",
                  }}
                >
                  <Typography variant="body2" color="inherit">
                    {page + 1}
                  </Typography>
        </Box>
        <IconButton
          onClick={(e) => !isFirstPage && onPageChange(e, 0)}
          disabled={isFirstPage}
          sx={{
            width: 32,
            height: 32,
            borderRadius: "50%",
            backgroundColor: isFirstPage ? "grey.500" : "black",
            color: isFirstPage ? "grey.300" : "white",
            "&:hover": {
               backgroundColor: isFirstPage ? "grey.500" : "black",
            },
          }}
        >
          <FirstPage />
        </IconButton>
        <IconButton
            onClick={(e) => !isFirstPage && onPageChange(e, page - 1)}
            disabled={isFirstPage}
            sx={{
                width: 32,
                height: 32,
                borderRadius: "50%",
                backgroundColor: isFirstPage ? "grey.500" : "black",
                color: isFirstPage ? "grey.300" : "white",
                "&:hover": {
                  backgroundColor: isFirstPage ? "grey.500" : "black",
                },
            }}>
            <KeyboardArrowLeft />
        </IconButton>

        <IconButton
          onClick={(e) => !isLastPage && onPageChange(e, page + 1)}
          disabled={isLastPage}
          sx={{
             width: 32,
             height: 32,
             borderRadius: "50%",
             backgroundColor: isLastPage ? "grey.500" : "black",
             color: isLastPage ? "grey.300" : "white",
             "&:hover": {
                backgroundColor: isLastPage ? "grey.500" : "black",
             },
          }}
        >
          <KeyboardArrowRight />
        </IconButton>
        <IconButton
          onClick={(e) => !isLastPage && onPageChange(e, Math.ceil(count / rowsPerPage) - 1)}
          disabled={isLastPage}
          sx={{
            width: 32,
            height: 32,
            borderRadius: "50%",
            backgroundColor: isLastPage ? "grey.500" : "black",
            color: isLastPage ? "grey.300" : "white",
            "&:hover": {
              backgroundColor: isLastPage ? "grey.500" : "black",
            },
          }}
        >
          <LastPage />
        </IconButton>
      </Box>
    </Box>
  );
};

export default CustomTablePagination;
