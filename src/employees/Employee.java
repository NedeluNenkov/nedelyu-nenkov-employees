package employees;

import java.time.LocalDate;

public class Employee {

	private String employeeId;
	private String projectId;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	
	public Employee(String employeeId, String projectId, LocalDate dateFrom, LocalDate dateTo) {
		this.employeeId = employeeId;
		this.projectId = projectId;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getProjectId() {
		return projectId;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}
}
