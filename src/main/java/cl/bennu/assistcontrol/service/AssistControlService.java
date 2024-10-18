package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.assistcontrol.mapper.BranchMapper;
import cl.bennu.assistcontrol.mapper.CommuneMapper;
import cl.bennu.assistcontrol.mapper.CompanyMapper;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@ApplicationScoped
public class AssistControlService {

    @Inject
    private CommuneMapper communeMapper;

    @Inject
    private CompanyMapper companyMapper;

    @Inject
    private BranchMapper branchMapper;

    public Commune getCommuneById(String token, Long communeId) {
        return communeMapper.get(communeId);
    }

    public List<Commune> getAllCommune(String token) {
        return communeMapper.getAll();
    }

    public List<Commune> findCommuneByQuery(String token, CommuneQuery query) {
        return communeMapper.findByQuery(query);
    }

    public void saveCommune(String token, Commune commune, String method) throws NoDataException, UniqueException {
        validate(token, commune, method);

        if (commune.getId() == null) {
            communeMapper.insert(commune);
        } else {
            communeMapper.update(commune);
        }
    }

    private void validate(String token, Commune commune, String method) throws NoDataException, UniqueException {
        // validaciones comunes
        if (commune.getName() == null || StringUtils.isBlank(commune.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (commune.getCityId() == null) {
            throw new NoDataException("No se especificó el campo ciudad");
        }

        CommuneQuery query = new CommuneQuery();
        query.setName(commune.getName());
        Commune communeDB = communeMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            // validaciones especificas de insert
            if (commune.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (communeDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {
            // validaciones especificas de update
            if (commune.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (communeDB != null && !communeDB.getId().equals(commune.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }

    public void deleteCommune(String token, Long communeId) {
        communeMapper.delete(communeId);
    }

    // COMPANY

    public Company getCompanyById(String token, Long companyId) {
        return companyMapper.get(companyId);
    }

    public List<Company> getAllCompany(String token) {
        return companyMapper.getAll();
    }

    public List<Company> findCompanyByQuery(String token, CompanyQuery query) {
        return companyMapper.findByQuery(query);
    }

    public void saveCompany(String token, Company company, String method) throws NoDataException, UniqueException {
        validateCompany(token, company, method);

        if (company.getId() == null) {
            companyMapper.insert(company);
        } else {
            companyMapper.update(company);
        }
    }


    private void validateCompany(String token, Company company, String method) throws NoDataException, UniqueException {
        // Validaciones comunes
        if (company.getCode() == null || StringUtils.isBlank(company.getCode())) {
            throw new NoDataException("No se especificó el campo codigo");
        }
        if (company.getName() == null || StringUtils.isBlank(company.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (company.getAddress() == null || StringUtils.isBlank(company.getAddress())) {
            throw new NoDataException("No se especificó el campo direccion");
        }
        if (company.getCommuneId() == null) {
            throw new NoDataException("No se especificó el campo comuna");
        }

        CompanyQuery query = new CompanyQuery();
        query.setCode(company.getCode());
        Company companyDB = companyMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (company.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (companyDB != null) {
                throw new UniqueException("La compañía ya existe");
            }
        } else {
            if (company.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (companyDB != null && !companyDB.getId().equals(company.getId())) {
                throw new UniqueException("La compañía ya existe");
            }
        }
    }

    public Company deleteCompanyById(String token, Long companyId) throws NoDataException {
        Company company = companyMapper.get(companyId);
        if (company == null) {
            throw new NoDataException("No se encontró la compañía con el id especificado");
        }
        companyMapper.delete(companyId);
        return company;
    }
    // BRANCH

    public Branch getBranchyById(String token, Long branchId) {
        return branchMapper.get(branchId);
    }

    public List<Branch> getAllBranch(String token) {
        return branchMapper.getAll();
    }

    public List<Branch> findBranchByQuery(String token, BranchQuery query) {
        return branchMapper.findByQuery(query);
    }

    public void saveBranch(String token, Branch branch, String method) throws NoDataException, UniqueException {
        validateBranch(token, branch, method);

        if (branch.getId() == null) {
            branchMapper.insert(branch);
        } else {
            branchMapper.update(branch);
        }
    }

    private void validateBranch(String token, Branch branch, String method) throws NoDataException, UniqueException {
        // Validaciones comunes Branch
        if (branch.getName() == null || StringUtils.isBlank(branch.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (branch.getAddress() == null || StringUtils.isBlank(branch.getAddress())) {
            throw new NoDataException("No se especificó el campo direccion");
        }
        if (branch.getCompanyId() == null) {
            throw new NoDataException("No se especificó el campo compañia");
        }


        BranchQuery query = new BranchQuery();
        query.setName(branch.getName());
        Branch branchDB = branchMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (branch.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (branchDB != null) {
                throw new UniqueException("La compañía ya existe");
            }
        } else {
            if (branch.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (branchDB != null && !branchDB.getId().equals(branch.getId())) {
                throw new UniqueException("La compañía ya existe");
            }
        }
    }

    public Branch deleteBranchById(String token, Long branchId) throws NoDataException {
        Branch branch = branchMapper.get(branchId);
        if (branch == null) {
            throw new NoDataException("No se encontró la compañía con el id especificado");
        }
        branchMapper.delete(branchId);
        return branch;
    }

}