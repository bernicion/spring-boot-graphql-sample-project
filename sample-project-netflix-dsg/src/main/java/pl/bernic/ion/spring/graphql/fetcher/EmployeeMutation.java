package pl.bernic.ion.spring.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import pl.bernic.ion.spring.graphql.domain.Department;
import pl.bernic.ion.spring.graphql.domain.Employee;
import pl.bernic.ion.spring.graphql.domain.EmployeeInput;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.DepartmentRepository;
import pl.bernic.ion.spring.graphql.repository.EmployeeRepository;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@DgsComponent
public class EmployeeMutation {

    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;
    OrganizationRepository organizationRepository;

    EmployeeMutation(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    public Employee addEmployee(@InputArgument("input") EmployeeInput employeeInput) {
        Department department = departmentRepository.findById(employeeInput.getDepartmentId()).orElseThrow();
        Organization organization = organizationRepository.findById(employeeInput.getOrganizationId()).orElseThrow();
        return employeeRepository.save(new Employee(null, employeeInput.getFirstName(), employeeInput.getLastName(),
                employeeInput.getPosition(), employeeInput.getAge(), employeeInput.getSalary(),
                department, organization));
    }


}
