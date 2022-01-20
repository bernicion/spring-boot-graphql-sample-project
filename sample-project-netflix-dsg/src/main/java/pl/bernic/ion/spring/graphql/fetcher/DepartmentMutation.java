package pl.bernic.ion.spring.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import pl.bernic.ion.spring.graphql.domain.Department;
import pl.bernic.ion.spring.graphql.domain.DepartmentInput;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.DepartmentRepository;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@DgsComponent
public class DepartmentMutation {

    private DepartmentRepository departmentRepository;
    private OrganizationRepository organizationRepository;

    DepartmentMutation(DepartmentRepository departmentRepository,
                       OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
    }

    @DgsData(parentType = "MutationResolver", field = "newDepartment")
    public Department newDepartment(DepartmentInput input) {
        Organization organization = organizationRepository
                .findById(input.getOrganizationId())
                .orElseThrow();
        return departmentRepository
                .save(new Department(null, input.getName(), null, organization));
    }
}
