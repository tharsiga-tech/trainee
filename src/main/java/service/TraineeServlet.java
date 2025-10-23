package service;

import com.google.gson.Gson;
import dao.TraineeDao;
import model.Trainee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/trainees")
public class TraineeServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final TraineeDao traineeDao = new TraineeDao();

    // Create trainee
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try (BufferedReader reader = request.getReader()) {
            Trainee trainee = gson.fromJson(reader, Trainee.class);

            if (trainee == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Invalid Trainee\"}");
                return;
            }

            boolean isSaved = traineeDao.saveTrainee(trainee);
            System.out.println("Trainee saved: " + isSaved);

            if (isSaved) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Trainee saved successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Failed to insert Trainee\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Invalid JSON: " + e.getMessage() + "\"}");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try {
            String idParam = request.getParameter("id");

            if (idParam != null) {
                // Get trainee by ID
                int id = Integer.parseInt(idParam);
                Trainee trainee = traineeDao.getTraineeById(id);

                if (trainee != null) {
                    response.getWriter().write(gson.toJson(trainee));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Trainee not found\"}");
                }

            } else {
                // Get all trainees
                List<Trainee> trainees = traineeDao.getAllTrainees();
                response.getWriter().write(gson.toJson(trainees));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to fetch trainee(s): " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try {
            String idParam = request.getParameter("id");

            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Missing trainee ID\"}");
                return;
            }

            int id = Integer.parseInt(idParam);
            boolean isDeleted = traineeDao.deleteTrainee(id);

            if (isDeleted) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Trainee deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Trainee not found or could not be deleted\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to delete trainee: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try (BufferedReader reader = request.getReader()) {
            Trainee trainee = gson.fromJson(reader, Trainee.class);

            if (trainee == null || trainee.getId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Trainee ID and details are required for update\"}");
                return;
            }

            boolean isUpdated = traineeDao.updateTrainee(trainee);

            if (isUpdated) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Trainee updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Trainee not found or update failed\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Failed to update trainee: " + e.getMessage() + "\"}");
        }
    }


//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//
//        try {
//            String idParm = request.getParameter("id");
//
//            if(idParm == null) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                out.write("{\"error\":\"Trainee ID is required to update\"}");
//                return;;
//            }
//
//            int id = Integer.parseInt(idParm);
//
//            BufferedReader reader = request.getReader();
//            Trainee updateTrainee = gson.fromJson(reader, Trainee.class);
//
//            if(updateTrainee == null) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                out.write("{\"message\":\"Invalid Trainee Data\"}");
//                return;
//            }
//
//            boolean isUpdated = traineeDao.updateTrainee(updateTrainee, id);
//            System.out.print(isUpdated);
//
//            if(isUpdated) {
//                response.setStatus(HttpServletResponse.SC_OK);
//                out.write("{\"message\":\"Trainee updated successfully\"}");
//            } else {
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                response.getWriter().write("{\"error\":\"Trainee not found or update failed\"}");
//            }
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\":\"Failed to update trainee: " + e.getMessage() + "\"}");
//        }
//    }
}
