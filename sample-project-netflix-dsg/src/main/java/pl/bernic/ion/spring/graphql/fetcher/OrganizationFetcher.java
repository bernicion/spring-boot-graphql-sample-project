package pl.bernic.ion.spring.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import pl.bernic.ion.spring.graphql.domain.Organization;
import pl.bernic.ion.spring.graphql.repository.OrganizationRepository;

@DgsComponent
public class OrganizationFetcher {
    private OrganizationRepository repository;

    @DgsData(parentType = "QueryResolver", field = "organizations")
    public Iterable<Organization> findAll() {
        return repository.findAll();
    }

    @DgsData(parentType = "QueryResolver", field = "organization")
    public Organization findById(@InputArgument("id") Integer id) {
        return repository.findById(id).orElseThrow(DgsEntityNotFoundException::new);
    }
}
