package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.*;
import cl.bennu.assistcontrol.domain.query.*;
import cl.bennu.assistcontrol.mapper.*;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
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

    @Inject
    private CityMapper cityMapper;

    @Inject
    private CountryMapper countryMapper;

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
        validateCommune(token, commune, method);

        if (commune.getId() == null) {
            communeMapper.insert(commune);
        } else {
            communeMapper.update(commune);
        }
    }

    private void validateCommune(String token, Commune commune, String method) throws NoDataException, UniqueException {
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

    public void saveCompany(String token, SaveCompanyRequest saveCompanyRequest, String method) throws NoDataException, UniqueException {
        Company company = saveCompanyRequest.getCompany();
        Branch branch = saveCompanyRequest.getBranch();
        Boolean sucursalRaiz = saveCompanyRequest.getSucursalRaiz();

        validateCompany(token, company, method);

        if (company.getId() == null) {
            companyMapper.insert(company);
        } else {
            companyMapper.update(company);
        }

        Long companyId = company.getId();
        if (companyId == null) {
            throw new NoDataException("Error al guardar la compañía: no se pudo generar un ID.");
        }

        if (Boolean.FALSE.equals(sucursalRaiz)) {
            branch.setName(company.getName());
            branch.setAddress(company.getAddress());
            branch.setPhone(company.getPhone());
            branch.setCompanyId(companyId);
            branch.setActive(false);
            saveBranch(token, branch, HttpMethod.POST);
        } else if (Boolean.TRUE.equals(branch.getActive())) {
            branch.setCompanyId(companyId);
            if (branch.getId() == null) {
                saveBranch(token, branch, HttpMethod.POST);
            } else {
                saveBranch(token, branch, HttpMethod.PUT);
            }
        }
    }






    private void validateCompany(String token, Company company, String method) throws NoDataException, UniqueException {
        // Validaciones comunes

        if (company.getCode() == null || StringUtils.isBlank(company.getCode())) {
            throw new NoDataException("No se especificó el campo codigo");
        }
        if (company.getName() == null || StringUtils.isBlank(company.getName())) {
            throw new NoDataException("No se especificó el campo nombre compañia");
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
            throw new NoDataException("No se encontró la compañía con el ID especificado: " + companyId);
        }

        BranchQuery branchQuery = new BranchQuery();
        branchQuery.setCompanyId(companyId);

        List<Branch> branches = findBranchByQuery(token, branchQuery);
        if (branches != null && !branches.isEmpty()) {
            throw new NoDataException("No se puede eliminar la compañía porque tiene sucursales asociadas.");
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

        if (Boolean.TRUE.equals(branch.getActive())) {
            if (branch.getName() == null || StringUtils.isBlank(branch.getName())) {
                throw new NoDataException("No se especificó el campo nombre sucursal");
            }
            if (branch.getAddress() == null || StringUtils.isBlank(branch.getAddress())) {
                throw new NoDataException("No se especificó el campo direccion");
            }
            if (branch.getCompanyId() == null) {
                throw new NoDataException("No se especificó el campo compañia");
            }
            if (branch.getPhone() == null) {
                throw new NoDataException("No se especificó el campo telefono");
            }
        }

        BranchQuery query = new BranchQuery();
        query.setName(branch.getName());
        Branch branchDB = branchMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (branch.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (branchDB != null) {
                throw new UniqueException("La sucursal ya existe");
            }
        } else {
            if (branchDB != null && !branchDB.getId().equals(branch.getId())) {
                throw new UniqueException("La sucursal ya existe");
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

    // CITY *******************************************************************************************************

    public City getCityById(String token, Long cityId) {
        return cityMapper.get(cityId);
    }

    public List<City> getAllCity(String token) {
        return cityMapper.getAll();
    }

    public List<City> findCityByQuery(String token, CityQuery query) {
        return cityMapper.findByQuery(query);
    }

    public void saveCity(String token, City city, String method) throws NoDataException, UniqueException {
        validateCity(token, city, method);

        if (city.getId() == null) {
            cityMapper.insert(city);
        } else {
            cityMapper.update(city);
        }
    }

    private void validateCity(String token, City city, String method) throws NoDataException, UniqueException {
        // validaciones comunes
        if (city.getName() == null || StringUtils.isBlank(city.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (city.getCountryId() == null) {
            throw new NoDataException("No se especificó el campo pais");
        }

        CityQuery query = new CityQuery();
        query.setName(city.getName());
        City cityDB = cityMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            // validaciones especificas de insert
            if (city.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (cityDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {
            // validaciones especificas de update
            if (city.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (cityDB != null && !cityDB.getId().equals(city.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }

    public void deleteCity(String token, Long cityId) {
        cityMapper.delete(cityId);
    }

    // COUNTRY *******************************************************************************************************

    public Country getCountryById(String token, Long countryId) {
        return countryMapper.get(countryId);
    }

    public List<Country> getAllCountry(String token) {
        return countryMapper.getAll();
    }

    public List<Country> findCountryByQuery(String token, CountryQuery query) {
        return countryMapper.findByQuery(query);
    }

    public void saveCountry(String token, Country country, String method) throws NoDataException, UniqueException {
        validateCountry(token, country, method);

        if (country.getId() == null) {
            countryMapper.insert(country);
        } else {
            countryMapper.update(country);
        }
    }

    private void validateCountry(String token, Country country, String method) throws NoDataException, UniqueException {
        // validaciones comunes
        if (country.getName() == null || StringUtils.isBlank(country.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (country.getNationality() == null) {
            throw new NoDataException("No se especificó el campo nacionalidad");
        }

        CountryQuery query = new CountryQuery();
        query.setName(country.getName());
        Country countryDB = countryMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            // validaciones especificas de insert
            if (country.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (countryDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {
            // validaciones especificas de update
            if (country.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (countryDB != null && !countryDB.getId().equals(country.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }

    public void deleteCountry(String token, Long countryId) {
        countryMapper.delete(countryId);
    }

}