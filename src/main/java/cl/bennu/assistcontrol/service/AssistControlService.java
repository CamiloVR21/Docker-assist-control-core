package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.*;
import cl.bennu.assistcontrol.domain.query.*;
import cl.bennu.assistcontrol.enums.JobTypeEnum;
import cl.bennu.assistcontrol.mapper.*;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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

    @Inject
    private RegionMapper regionMapper;

    @Inject
    private JobSchedulerMapper jobSchedulerMapper;

    @Inject
    private EmployeeMapper employeeMapper;

    public Commune getCommuneById(String token, Commune commune) throws NoDataException {
        if (commune == null || commune.getId() == null) {
            throw new NoDataException("No se especificó el ID de la comuna.");
        }
        Long communeId = commune.getId();
        Commune result = communeMapper.get(communeId);

        if (result == null) {
            throw new NoDataException("No se encontró la comuna con el ID especificado: " + communeId);
        }

        return result;
    }


    public List<Commune> getAllCommune(String token) {
        return communeMapper.getAll();
    }

    public List<Commune> findCommuneByQuery(String token, CommuneQuery query) {
        return communeMapper.findByQuery(query);

    }

    @Transactional
    public void saveCommune(String token, Commune commune, String method) throws NoDataException, UniqueException {
        validateCommune(token, commune, method);

        if (commune.getId() == null) {
            communeMapper.insert(commune);
        } else {
            communeMapper.update(commune);
        }
    }

    private void validateCommune(String token, Commune commune, String method) throws NoDataException, UniqueException {

        if (commune.getName() == null || StringUtils.isBlank(commune.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (commune.getCity() == null || commune.getCity().getId() == null) {
            throw new NoDataException("No se especificó el campo ciudad");
        }

        CommuneQuery query = new CommuneQuery();
        query.setName(commune.getName());
        Commune communeDB = communeMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (commune.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (communeDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {
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

    // COMPANY *******************************************************************************************************

    public Company getCompanyById(String token, Company company) throws NoDataException {
        if (company == null || company.getId() == null) {
            throw new NoDataException("No se especificó el ID de la compañía.");
        }
        Company foundCompany = companyMapper.get(company.getId());
        if (foundCompany == null) {
            throw new NoDataException("No se encontró la compañía con el ID especificado: " + company.getId());
        }
        return foundCompany;

    }

    public List<Company> getAllCompany(String token) {
        return companyMapper.getAll();
    }

    public List<Company> findCompanyByQuery(String token, CompanyQuery query) {
        return companyMapper.findByQuery(query);
    }

    @Transactional
    public void saveCompany(String token, SaveCompanyRequest saveCompanyRequest, String method) throws NoDataException, UniqueException {
        Company company = saveCompanyRequest.getCompany();
        Branch branch = saveCompanyRequest.getBranch();
        Boolean hq = saveCompanyRequest.getHq();
        if (company == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de la compañía");
        }
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
        if (Boolean.TRUE.equals(hq)) {
            if (branch == null || branch.getId() == null) {
                Branch existingBranch = findBranchByCompany(company);
                if (existingBranch == null) {
                    branch = new Branch();
                    branch.setName(company.getName());
                    branch.setAddress(company.getAddress());
                    branch.setPhone(company.getPhone());
                    branch.setCompany(company);
                    branch.setActive(true);
                    saveBranch(token, branch, HttpMethod.POST);
                } else {
                    existingBranch.setName(company.getName());
                    existingBranch.setAddress(company.getAddress());
                    existingBranch.setPhone(company.getPhone());
                    existingBranch.setAlias(company.getAlias());
                    existingBranch.setCompany(company);
                    existingBranch.setActive(true);
                    saveBranch(token, existingBranch, HttpMethod.PUT);
                }
            } else {
                branch.setCompany(company);
                saveBranch(token, branch, branch.getId() == null ? HttpMethod.POST : HttpMethod.PUT);
            }
        } else if (hq && branch != null) {
            branch.setCompany(company);
            saveBranch(token, branch, branch.getId() == null ? HttpMethod.POST : HttpMethod.PUT);
        }
    }

    private void validateCompany(String token, Company company, String method) throws NoDataException, UniqueException {

        if (company == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de la compañía");
        }

        if (company.getCode() == null || StringUtils.isBlank(company.getCode())) {
            throw new NoDataException("No se especificó el campo código");
        }
        if (company.getName() == null || StringUtils.isBlank(company.getName())) {
            throw new NoDataException("No se especificó el campo nombre de la compañía");
        }
        if (company.getAddress() == null || StringUtils.isBlank(company.getAddress())) {
            throw new NoDataException("No se especificó el campo dirección");
        }
        if (company.getCommune() == null || company.getCommune().getId() == null) {
            throw new NoDataException("No se especificó el campo comuna");
        }
        if (company.getGeolocation() == null){
            throw new NoDataException("No se especificó el campo geolocalización");
        }
        if (company.getLag() == null) {
            throw new NoDataException("No se especificó el campo lag");
        }
        if (company.getSelfie() == null){
            throw new NoDataException("No se especificó el campo selfie");
        }

        CompanyQuery query = new CompanyQuery();
        query.setCode(company.getCode());
        Company companyDB = companyMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {

            if (companyDB != null) {
                throw new UniqueException("La compañía ya existe");
            }
        } else {
            if (company.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar la compañía");
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
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(companyId);
        branchQuery.setCompanyId(companyQuery);

        List<Branch> branches = findBranchByQuery(token, branchQuery);
        if (branches != null && !branches.isEmpty()) {
            throw new NoDataException("No se puede eliminar la compañía porque tiene sucursales asociadas.");
        }

        companyMapper.delete(companyId);
        return company;
    }
    public List<JobTypeEnum> findJobTypesByCompany(String token, Long companyId) throws NoDataException {
        if (companyId == null) {
            throw new NoDataException("No se especificó el ID de la compañía.");
        }
        List<JobTypeEnum> jobTypes = companyMapper.findJobTypesByCompany(companyId);
        if (jobTypes == null || jobTypes.isEmpty()) {
            throw new NoDataException("No se encontraron tipos de trabajo para la compañía con el ID especificado: " + companyId);
        }
        return jobTypes;
    }
    // BRANCH *******************************************************************************************************

    public Branch getBranchById(String token, Branch branch) throws NoDataException {
        if (branch == null || branch.getId() == null) {
            throw new NoDataException("No se especificó el ID de la sucursal.");
        }
        Branch foundBranch = branchMapper.get(branch.getId());
        if (foundBranch == null) {
            throw new NoDataException("No se encontró la sucursal con el ID especificado: " + branch.getId());
        }
        return foundBranch;
    }

    public List<Branch> getAllBranch(String token) {
        return branchMapper.getAll();
    }

    public List<Branch> findBranchByQuery(String token, BranchQuery query) {
        return branchMapper.findByQuery(query);
    }

    @Transactional
    public void saveBranch(String token, Branch branch, String method) throws NoDataException, UniqueException {
        validateBranch(token, branch, method);

        if (branch.getId() == null) {
            branchMapper.insert(branch);
        } else {
            branchMapper.update(branch);
        }
    }

    private void validateBranch(String token, Branch branch, String method) throws NoDataException, UniqueException {
        if (branch == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de la sucursal");
        }

        if (Boolean.TRUE.equals(branch.getActive())) {
            if (branch.getName() == null || StringUtils.isBlank(branch.getName())) {
                throw new NoDataException("No se especificó el campo nombre de la sucursal");
            }
            if (branch.getAddress() == null || StringUtils.isBlank(branch.getAddress())) {
                throw new NoDataException("No se especificó el campo dirección de la sucursal");
            }
            if (branch.getCompany() == null || branch.getCompany().getId() == null) {
                throw new NoDataException("No se especificó el campo compañía de la sucursal");
            }
        }

        BranchQuery query = new BranchQuery();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(branch.getCompany().getId());
        query.setName(branch.getName());
        query.setCompanyId(companyQuery);
        List<Branch> branchDBList = branchMapper.findByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (branch.getId() != null) {
                throw new NoDataException("El campo ID debe ser nulo para insertar una nueva sucursal");
            }
            if (branchDBList != null && !branchDBList.isEmpty()) {
                throw new UniqueException("La sucursal ya existe");
            }
        } else if (HttpMethod.PUT.equalsIgnoreCase(method)) {
            if (branch.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar la sucursal");
            }
            if (branchDBList != null && !branchDBList.isEmpty()) {
                for (Branch existingBranch : branchDBList) {
                    if (!existingBranch.getId().equals(branch.getId())) {
                        throw new UniqueException("La sucursal ya existe con el mismo nombre");
                    }
                }
            }
        }
    }

    public Branch deleteBranchById(String token, Long branchId) throws NoDataException {
        Branch branch = branchMapper.get(branchId);
        if (branch == null) {
            throw new NoDataException("No se encontró la sucursal con el ID especificado");
        }

        EmployeeQuery employeeQuery = new EmployeeQuery();
        BranchQuery branchQuery = new BranchQuery();
        branchQuery.setId(branchId);
        employeeQuery.setBranch(branchQuery);

        List<Employee> employees = findEmployeesByQuery(token, employeeQuery);
        if (employees != null && !employees.isEmpty()) {
            throw new NoDataException("No se puede eliminar la sucursal porque tiene empleados asociados.");
        }

        branchMapper.delete(branchId);
        return branch;
    }



    private Branch findBranchByCompany(Company company) throws NoDataException {
        BranchQuery branchQuery = new BranchQuery();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(company.getId());
        branchQuery.setCompanyId(companyQuery);

        List<Branch> branches = findBranchByQuery("", branchQuery);
        if (branches != null && !branches.isEmpty()) {
            return branches.get(0);
        }
        return null;
    }



    // CITY *******************************************************************************************************

    public City getCityById(String token, City city) throws NoDataException {
        if (city == null || city.getId() == null) {
            throw new NoDataException("No se especificó el ID de la ciudad.");
        }
        City foundCity = cityMapper.get(city.getId());
        if (foundCity == null) {
            throw new NoDataException("No se encontró la ciudad con el ID especificado: " + city.getId());
        }
        return foundCity;
    }

    public List<City> getAllCity(String token) {
        return cityMapper.getAll();
    }

    public List<City> findCityByQuery(String token, CityQuery query) {
        return cityMapper.findByQuery(query);
    }

    @Transactional
    public void saveCity(String token, City city, String method) throws NoDataException, UniqueException {
        validateCity(token, city, method);

        if (city.getId() == null) {
            cityMapper.insert(city);
        } else {
            cityMapper.update(city);
        }
    }

    private void validateCity(String token, City city, String method) throws NoDataException, UniqueException {

        if (city.getName() == null || StringUtils.isBlank(city.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (city.getRegion() == null || city.getRegion().getId() == null) {
            throw new NoDataException("No se especificó el campo region");
        }

        CityQuery query = new CityQuery();
        query.setName(city.getName());
        City cityDB = cityMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {

            if (city.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (cityDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {

            if (city.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (cityDB != null && !cityDB.getId().equals(city.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }


    // COUNTRY *******************************************************************************************************

    public Country getCountryById(String token, Country country) throws NoDataException {
        if (country == null || country.getId() == null) {
            throw new NoDataException("No se especificó el ID del país.");
        }
        Country foundCountry = countryMapper.get(country.getId());
        if (foundCountry == null) {
            throw new NoDataException("No se encontró el país con el ID especificado: " + country.getId());
        }
        return foundCountry;
    }

    public List<Country> getAllCountry(String token) {
        return countryMapper.getAll();
    }

    public List<Country> findCountryByQuery(String token, CountryQuery query) {
        return countryMapper.findByQuery(query);
    }

    @Transactional
    public void saveCountry(String token, Country country, String method) throws NoDataException, UniqueException {
        validateCountry(token, country, method);

        if (country.getId() == null) {
            countryMapper.insert(country);
        } else {
            countryMapper.update(country);
        }
    }

    private void validateCountry(String token, Country country, String method) throws NoDataException, UniqueException {

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

            if (country.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (countryDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {

            if (country.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (countryDB != null && !countryDB.getId().equals(country.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }


    // Region *******************************************************************************************************

    public Region getRegionById(String token, Region region) throws NoDataException {
        if (region == null || region.getId() == null) {
            throw new NoDataException("No se especificó el ID de la región.");
        }
        Region foundRegion = regionMapper.get(region.getId());
        if (foundRegion == null) {
            throw new NoDataException("No se encontró la región con el ID especificado: " + region.getId());
        }
        return foundRegion;
    }

    public List<Region> getAllRegion(String token) {
        return regionMapper.getAll();
    }

    public List<Region> findRegionByQuery(String token, RegionQuery query) {
        return regionMapper.findByQuery(query);
    }

    @Transactional
    public void saveRegion(String token, Region region, String method) throws NoDataException, UniqueException {
        validateRegion(token, region, method);

        if (region.getId() == null) {
            regionMapper.insert(region);
        } else {
            regionMapper.update(region);
        }
    }

    private void validateRegion(String token, Region region, String method) throws NoDataException, UniqueException {

        if (region.getName() == null || StringUtils.isBlank(region.getName())) {
            throw new NoDataException("No se especificó el campo nombre");
        }
        if (region.getCountry() == null) {
            throw new NoDataException("No se especificó el campo pais");
        }

        RegionQuery query = new RegionQuery();
        query.setName(region.getName());
        Region regionDB = regionMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {

            if (region.getId() != null) {
                throw new NoDataException("El campo id debe ser nulo");
            }
            if (regionDB != null) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        } else {

            if (region.getId() == null) {
                throw new NoDataException("No se especificó el campo id");
            }
            if (regionDB != null && !regionDB.getId().equals(region.getId())) {
                throw new UniqueException("El tramo de riesgo ya existe");
            }
        }
    }

    // JobScheduler *******************************************************************************************************
    public JobScheduler getJobSchedulerById(String token, JobScheduler jobScheduler) throws NoDataException {
        if (jobScheduler == null || jobScheduler.getId() == null) {
            throw new NoDataException("No se especificó el ID del programador de trabajos.");
        }
        JobScheduler foundJobScheduler = jobSchedulerMapper.get(jobScheduler.getId());
        if (foundJobScheduler == null) {
            throw new NoDataException("No se encontró el programador de trabajos con el ID especificado: " + jobScheduler.getId());
        }
        return foundJobScheduler;
    }

    public List<JobScheduler> getAllJobScheduler(String token) {
        return jobSchedulerMapper.getAll();
    }

    public List<JobScheduler> findJobSchedulerByQuery(String token, JobSchedulerQuery query) {
        return jobSchedulerMapper.findByQuery(query);
    }

    @Transactional
    public void saveJobScheduler(String token, JobScheduler jobScheduler, String method) throws NoDataException, UniqueException {
        validateJobScheduler(token, jobScheduler, method);

        if (jobScheduler.getId() == null) {
            jobSchedulerMapper.insert(jobScheduler);
        } else {
            jobSchedulerMapper.update(jobScheduler);
        }
    }

    private void validateJobScheduler(String token, JobScheduler jobScheduler, String method) throws NoDataException, UniqueException {
        if (jobScheduler == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del programador de trabajos");
        }

        if (StringUtils.isBlank(jobScheduler.getName())) {
            throw new NoDataException("No se especificó el nombre del programador de trabajos");
        }
        if (jobScheduler.getMonday() == null) {
            throw new NoDataException("No se especificó el campo lunes");
        }
        if (jobScheduler.getTuesday() == null) {
            throw new NoDataException("No se especificó el campo martes");
        }
        if (jobScheduler.getWednesday() == null) {
            throw new NoDataException("No se especificó el campo miércoles");
        }
        if (jobScheduler.getThursday() == null) {
            throw new NoDataException("No se especificó el campo jueves");
        }
        if (jobScheduler.getFriday() == null) {
            throw new NoDataException("No se especificó el campo viernes");
        }
        if (jobScheduler.getSaturday() == null) {
            throw new NoDataException("No se especificó el campo sábado");
        }
        if (jobScheduler.getSunday() == null) {
            throw new NoDataException("No se especificó el campo domingo");
        }

        JobSchedulerQuery query = new JobSchedulerQuery();
        query.setName(jobScheduler.getName());
        List<JobScheduler> jobSchedulerDBList = jobSchedulerMapper.findByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (jobScheduler.getId() != null) {
                throw new NoDataException("El campo ID debe ser nulo para insertar un nuevo programador de trabajos");
            }
            if (jobSchedulerDBList != null && !jobSchedulerDBList.isEmpty()) {
                throw new UniqueException("El programador de trabajos ya existe");
            }
        } else if (HttpMethod.PUT.equalsIgnoreCase(method)) {
            if (jobScheduler.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar el programador de trabajos");
            }
            if (jobSchedulerDBList != null && !jobSchedulerDBList.isEmpty()) {
                for (JobScheduler existingJobScheduler : jobSchedulerDBList) {
                    if (!existingJobScheduler.getId().equals(jobScheduler.getId())) {
                        throw new UniqueException("El programador de trabajos ya existe con el mismo nombre");
                    }
                }
            }
        }
    }

    public JobScheduler deleteJobSchedulerById(String token, Long jobSchedulerId) throws NoDataException {
        JobScheduler jobScheduler = jobSchedulerMapper.get(jobSchedulerId);
        if (jobScheduler == null) {
            throw new NoDataException("No se encontró el horario de trabajo con el ID especificado: " + jobSchedulerId);
        }

        EmployeeQuery employeeQuery = new EmployeeQuery();
        JobSchedulerQuery jobSchedulerQuery = new JobSchedulerQuery();
        jobSchedulerQuery.setId(jobSchedulerId);
        employeeQuery.setJobScheduler(jobSchedulerQuery);

        List<Employee> employees = findEmployeesByQuery(token, employeeQuery);
        if (employees != null && !employees.isEmpty()) {
            throw new NoDataException("No se puede eliminar el horario de trabajos porque tiene empleados asociados.");
        }

        jobSchedulerMapper.delete(jobSchedulerId);
        return jobScheduler;
    }



    // EMPLOYEEE *******************************************************************************************************

    public Employee getEmployeeById(String token, Long employeeId) throws NoDataException {
        if (employeeId == null) {
            throw new NoDataException("No se especificó el ID del empleado.");
        }
        Employee employee = employeeMapper.get(employeeId);
        if (employee == null) {
            throw new NoDataException("No se encontró el empleado con el ID especificado: " + employeeId);
        }
        return employee;
    }

    public List<Employee> getAllEmployees(String token) {
        return employeeMapper.getAll();
    }

    public List<Employee> findEmployeesByQuery(String token, EmployeeQuery query) {
        return employeeMapper.findByQuery(query);
    }

    public List<Employee> findEmployeesByCompany(String token, Long companyId) throws NoDataException {
        if (companyId == null) {
            throw new NoDataException("No se especificó el ID de la compañía.");
        }
        List<Employee> employees = employeeMapper.findByCompany(companyId);
        if (employees == null || employees.isEmpty()) {
            throw new NoDataException("No se encontraron empleados para la compañía con el ID especificado: " + companyId);
        }
        return employees;
    }


    @Transactional
    public void saveEmployee(String token, Employee employee, String method) throws NoDataException, UniqueException {
        validateEmployee(token, employee, method);

        if (employee.getId() == null) {
            employeeMapper.insert(employee);
        } else {
            employeeMapper.update(employee);
        }
    }

    private void validateEmployee(String token, Employee employee, String method) throws NoDataException, UniqueException {
        if (employee == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del empleado");
        }

        if (StringUtils.isBlank(employee.getName())) {
            throw new NoDataException("No se especificó el nombre del empleado");
        }
        if (StringUtils.isBlank(employee.getLastName())) {
            throw new NoDataException("No se especificó el apellido del empleado");
        }
        if (employee.getGender() == null) {
            throw new NoDataException("No se especificó el género del empleado");
        }
        if (employee.getCommune() == null || employee.getCommune().getId() == null) {
            throw new NoDataException("No se especificó la comuna del empleado");
        }
        if (employee.getBranch() == null || employee.getBranch().getId() == null) {
            throw new NoDataException("No se especificó la sucursal del empleado");
        }
        if (employee.getJobScheduler() == null || employee.getJobScheduler().getId() == null) {
            throw new NoDataException("No se especifico el horario del empleado");
        }
        if (employee.getContractType() == null) {
            throw new NoDataException("No se especificó el tipo de trabajo del empleado");
        }
        if (employee.getJobType() == null) {
            throw new NoDataException("No se especificó el tipo de trabajo");
        }
        if (employee.getCountry() == null) {
            throw new NoDataException("No se especificó el pais del empleado");
        }
        if (employee.getMaritalStatus() == null) {
            throw new NoDataException("No se especificó el estado del empleado");
        }
        if (employee.getAddress() == null) {
            throw new NoDataException("No se especificó la dirreccion del empleado");
        }
        if (employee.getContractDate() == null) {
            throw new NoDataException("No se especificó la fecha del contrato del empleado");
        }
        if (employee.getCode() == null) {
            throw new NoDataException("No se especificó el codigo del empleado");
        }

        EmployeeQuery query = new EmployeeQuery();
        query.setName(employee.getName());
        query.setLastName(employee.getLastName());
        List<Employee> employeeDBList = employeeMapper.findByQuery(query);

        if ("POST".equalsIgnoreCase(method)) {
            if (employee.getId() != null) {
                throw new NoDataException("El campo ID debe ser nulo para insertar un nuevo empleado");
            }
            if (employeeDBList != null && !employeeDBList.isEmpty()) {
                throw new UniqueException("El empleado ya existe");
            }
            employee.setActive(true);
        } else if ("PUT".equalsIgnoreCase(method)) {
            if (employee.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar el empleado");
            }
            if (employeeDBList != null && !employeeDBList.isEmpty()) {
                for (Employee existingEmployee : employeeDBList) {
                    if (!existingEmployee.getId().equals(employee.getId())) {
                        throw new UniqueException("El empleado ya existe con el mismo nombre y apellido");
                    }
                }
            }
        }
    }

    public Employee deleteEmployeeById(String token, Long employeeId) throws NoDataException {
        Employee employee = employeeMapper.get(employeeId);
        if (employee == null) {
            throw new NoDataException("No se encontró el empleado con el ID especificado: " + employeeId);
        }
        if (employeeId == null) {
            throw new NoDataException("No se especifico el id del empleado: ");
        }
        employeeMapper.delete(employeeId);
        return employee;
    }
}