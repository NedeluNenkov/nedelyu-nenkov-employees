package employees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/getFile")
@MultipartConfig
public class FileServlet extends HttpServlet {
	// supported date formats
	private static final String[] formats = { "dd/MM/yy", "dd/MM/yyyy", "d/M/yy", "d/M/yyyy", "ddMMyy", "ddMMyyyy",
			"ddMMMyy", "ddMMMyyyy", "dd-MMM-yy", "dd-MMM-yyyy", "dMMMyy", "dMMMyyyy", "d-MMM-yy", "d-MMM-yyyy",
			"d-MMMM-yy", "d-MMMM-yyyy", "yyMMdd", "yyyyMMdd", "yy/MM/dd", "yyyy/MM/dd", "MMddyy", "MMddyyyy",
			"MM/dd/yy", "MM/dd/yyyy", "MMM-dd-yy", "yyyy-MM-dd", "MMM-dd-yyyy" };

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		try {
			String file = request.getParameter("file");
			BufferedReader br = new BufferedReader(new FileReader(file));

			HashMap<String, List<Employee>> employeesPerProject = new HashMap<>();

			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(", ");

				String employeeId = data[0];
				String projectId = data[1];
				LocalDate dateFrom = parse(data[2]);

				LocalDate dateTo = !data[3].equals("NULL") ? parse(data[3]) : LocalDate.now();
				Employee employee = new Employee(employeeId, projectId, dateFrom, dateTo);

				if (!employeesPerProject.containsKey(projectId)) {
					employeesPerProject.put(projectId, new ArrayList<>());
				}
				employeesPerProject.get(projectId).add(employee);

			}
			// collection of the max duration between two players for each project
			HashMap<Integer, Tuple<Employee, Employee>> maxDuration = new HashMap<>();
			String projectKey = "";
			int indexOne = 0;
			int indexTwo = 0;
			int maxDays = 0;
			for (Entry<String, List<Employee>> project : employeesPerProject.entrySet()) {
				//if there is only one person in a project, skip it
				if (project.getValue().size() <= 1) {
					continue;
				}
				for (int i = 0; i < project.getValue().size() - 1; i++) {
					for (int j = i + 1; j < project.getValue().size(); j++) {
						LocalDate startDate = project.getValue().get(i).getDateFrom()
								.compareTo(project.getValue().get(j).getDateFrom()) > 0
										? project.getValue().get(i).getDateFrom()
										: project.getValue().get(j).getDateFrom();

						LocalDate endDate = project.getValue().get(i).getDateTo()
								.compareTo(project.getValue().get(j).getDateTo()) < 0
										? project.getValue().get(i).getDateTo()
										: project.getValue().get(j).getDateTo();

						int days = (int) ChronoUnit.DAYS.between(startDate, endDate);

						if (maxDays < days) {
							indexOne = i;
							indexTwo = j;
							maxDays = days;
							projectKey = project.getKey();
						}
					}
				}
			}
			Employee employeeOne = employeesPerProject.get(projectKey).get(indexOne);
			Employee employeeTwo = employeesPerProject.get(projectKey).get(indexTwo);

			maxDuration.put(maxDays, new Tuple<Employee, Employee>(employeeOne, employeeTwo));
			session.setAttribute("error", null);
			session.setAttribute("result", maxDuration);
		} catch (IOException | IllegalArgumentException e) { 
			session.setAttribute("error", "error");
		}
		response.sendRedirect("result.jsp");
	}

	public static LocalDate parse(String date) {
		if (date != null) {
			for (String format : formats) {
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
					return LocalDate.parse(date, formatter);
				} catch (Exception e) {
					continue;
				}
			}
		}
		//if format is not supported throws exception which is then pressented to the user as error message
		throw new IllegalArgumentException();
	}

}
