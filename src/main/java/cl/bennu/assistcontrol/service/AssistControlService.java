package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.*;
import cl.bennu.assistcontrol.domain.query.*;
import cl.bennu.assistcontrol.mapper.*;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.assistcontrol.request.SaveRegisterRequest;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import java.sql.Time;
import java.util.List;

@ApplicationScoped
public class AssistControlService {

    // Inyección de mappers para operar sobre la base de datos
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
    @Inject
    private JobTypeMapper jobTypeMapper;
    @Inject
    private RegisterMapper registerMapper;

    // ==================== COMUNA ====================
    /**
     * Obtiene una comuna a partir de su ID.
     * Lanza excepción si no se especifica el ID o si no se encuentra la comuna.
     */
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

    /**
     * Retorna todas las comunas.
     */
    public List<Commune> getAllCommune(String token) {
        return communeMapper.getAll();
    }

    /**
     * Busca comunas basadas en los criterios del objeto query.
     */
    public List<Commune> findCommuneByQuery(String token, CommuneQuery query) {
        return communeMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza una comuna.
     * Valida la información antes de realizar la operación.
     */
    @Transactional
    public void saveCommune(String token, Commune commune, String method) throws NoDataException, UniqueException {
        validateCommune(token, commune, method);
        if (commune.getId() == null) {
            communeMapper.insert(commune);
        } else {
            communeMapper.update(commune);
        }
    }

    /**
     * Valida que la comuna tenga nombre y ciudad, y que el nombre sea único.
     * Para POST, el ID debe ser nulo; para PUT, el ID debe existir y no colisionar.
     */
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

    /**
     * Elimina una comuna a partir de su ID.
     */
    public void deleteCommune(String token, Long communeId) {
        communeMapper.delete(communeId);
    }

    // ==================== COMPAÑÍA ====================
    /**
     * Obtiene una compañía a partir de su ID.
     * Lanza excepción si no se especifica el ID o si no se encuentra la compañía.
     */
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

    /**
     * Retorna todas las compañías.
     */
    public List<Company> getAllCompany(String token) {
        return companyMapper.getAll();
    }

    /**
     * Busca compañías basadas en los criterios del objeto CompanyQuery.
     */
    public List<Company> findCompanyByQuery(String token, CompanyQuery query) {
        return companyMapper.findByQuery(query);
    }

    /**
     * Retorna compañías asociadas a un empleado a partir del ID del empleado.
     */
    public List<Company> getCompanyByEmployeeId(String token, Long employeeId) throws NoDataException {
        return companyMapper.getCompanyByEmployee(employeeId);
    }

    /**
     * Retorna compañías asociadas a un usuario de la aplicación a partir del ID del usuario.
     */
    public List<Company> getCompanyByAppUser(String token, Long appUserId) throws NoDataException {
        return companyMapper.getCompanyByAppUser(appUserId);
    }

    /**
     * Retorna las sucursales asociadas a una compañía a partir del ID de la compañía.
     * Lanza excepción si no se encuentran sucursales.
     */
    public List<Branch> getBranchesByCompany(Long companyId) throws NoDataException {
        BranchQuery branchQuery = new BranchQuery();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(companyId);
        branchQuery.setCompanyId(companyQuery);
        List<Branch> branches = branchMapper.findByQuery(branchQuery);
        if (branches == null || branches.isEmpty()) {
            throw new NoDataException("No se encontraron sucursales para la compañía con ID: " + companyId);
        }
        return branches;
    }

    /**
     * Guarda o actualiza la información de una compañía, junto con su sucursal (si es sede central).
     * Valida la información de la compañía y de la sucursal antes de la operación.
     */
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
        // Manejo de la sucursal si es sede central (hq)
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

    /**
     * Valida la información de una compañía antes de guardarla o actualizarla.
     * Verifica campos obligatorios como código, nombre, dirección, comuna y flags de geolocalización, lag y selfie.
     */
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
        if (company.getGeolocation() == null) {
            throw new NoDataException("No se especificó el campo geolocalización");
        }
        if (company.getLag() == null) {
            throw new NoDataException("No se especificó el campo lag");
        }
        if (company.getSelfie() == null) {
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

    /**
     * Elimina una compañía a partir de su ID, verificando que no tenga sucursales con empleados asociados.
     */
    public Company deleteCompanyById(String token, Long companyId) throws NoDataException {
        Company company = companyMapper.get(companyId);
        if (company == null) {
            throw new NoDataException("No se encontró la compañía con el ID especificado: " + companyId);
        }
        // Verifica si existen sucursales asociadas a la compañía
        BranchQuery branchQuery = new BranchQuery();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(companyId);
        branchQuery.setCompanyId(companyQuery);
        List<Branch> branches = findBranchByQuery(token, branchQuery);
        if (branches != null && !branches.isEmpty()) {
            for (Branch branch : branches) {
                // Verifica si la sucursal tiene empleados asociados
                EmployeeQuery employeeQuery = new EmployeeQuery();
                BranchQuery branchQueryForEmployee = new BranchQuery();
                branchQueryForEmployee.setId(branch.getId());
                employeeQuery.setBranch(branchQueryForEmployee);
                List<Employee> employees = findEmployeesByQuery(token, employeeQuery);
                if (employees != null && !employees.isEmpty()) {
                    throw new NoDataException("No se puede eliminar la compañía porque la sucursal " + branch.getName() + " tiene empleados asociados.");
                }
            }
            if (branches.size() == 1) {
                branchMapper.delete(branches.get(0).getId());
            } else {
                throw new NoDataException("No se puede eliminar la compañía porque tiene sucursales asociadas.");
            }
        }
        companyMapper.delete(companyId);
        return company;
    }

    // ==================== SUCURSAL (BRANCH) ====================
    /**
     * Obtiene una sucursal a partir de su ID.
     */
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

    /**
     * Retorna todas las sucursales.
     */
    public List<Branch> getAllBranch(String token) {
        return branchMapper.getAll();
    }

    /**
     * Busca sucursales basadas en criterios definidos en BranchQuery.
     */
    public List<Branch> findBranchByQuery(String token, BranchQuery query) {
        return branchMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza una sucursal, validando primero su información.
     */
    @Transactional
    public void saveBranch(String token, Branch branch, String method) throws NoDataException, UniqueException {
        validateBranch(token, branch, method);
        if (branch.getId() == null) {
            branchMapper.insert(branch);
        } else {
            branchMapper.update(branch);
        }
    }

    /**
     * Valida la información de la sucursal, asegurando que los campos obligatorios estén presentes
     * y que el nombre sea único según la compañía.
     */
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

    /**
     * Elimina una sucursal a partir de su ID, verificando que no tenga empleados asociados.
     */
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

    /**
     * Busca y retorna la primera sucursal asociada a una compañía.
     */
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

    // ==================== CIUDAD (CITY) ====================
    /**
     * Obtiene una ciudad a partir de su ID.
     */
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

    /**
     * Retorna todas las ciudades.
     */
    public List<City> getAllCity(String token) {
        return cityMapper.getAll();
    }

    /**
     * Busca ciudades según criterios definidos en CityQuery.
     */
    public List<City> findCityByQuery(String token, CityQuery query) {
        return cityMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza una ciudad, validando previamente la información.
     */
    @Transactional
    public void saveCity(String token, City city, String method) throws NoDataException, UniqueException {
        validateCity(token, city, method);
        if (city.getId() == null) {
            cityMapper.insert(city);
        } else {
            cityMapper.update(city);
        }
    }

    /**
     * Valida la información de la ciudad, asegurándose de que tenga nombre y región, y que el nombre sea único.
     */
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

    // ==================== PAÍS (COUNTRY) ====================
    /**
     * Obtiene un país a partir de su ID.
     */
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

    /**
     * Retorna todos los países.
     */
    public List<Country> getAllCountry(String token) {
        return countryMapper.getAll();
    }

    /**
     * Busca países según los criterios definidos en CountryQuery.
     */
    public List<Country> findCountryByQuery(String token, CountryQuery query) {
        return countryMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza la información de un país, validando previamente la información.
     */
    @Transactional
    public void saveCountry(String token, Country country, String method) throws NoDataException, UniqueException {
        validateCountry(token, country, method);
        if (country.getId() == null) {
            countryMapper.insert(country);
        } else {
            countryMapper.update(country);
        }
    }

    /**
     * Valida la información del país, asegurándose de que tenga nombre y nacionalidad, y que el nombre sea único.
     */
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

    // ==================== REGIÓN (REGION) ====================
    /**
     * Obtiene una región a partir de su ID.
     */
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

    /**
     * Retorna todas las regiones.
     */
    public List<Region> getAllRegion(String token) {
        return regionMapper.getAll();
    }

    /**
     * Busca regiones según los criterios definidos en RegionQuery.
     */
    public List<Region> findRegionByQuery(String token, RegionQuery query) {
        return regionMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza la información de una región, validando previamente la información.
     */
    @Transactional
    public void saveRegion(String token, Region region, String method) throws NoDataException, UniqueException {
        validateRegion(token, region, method);
        if (region.getId() == null) {
            regionMapper.insert(region);
        } else {
            regionMapper.update(region);
        }
    }

    /**
     * Valida la información de la región, asegurándose de que tenga nombre y país, y que el nombre sea único.
     */
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

    // ==================== PROGRAMADOR DE TRABAJOS (JOB SCHEDULER) ====================
    /**
     * Obtiene un programador de trabajos a partir de su ID.
     */
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

    /**
     * Retorna todos los programadores de trabajos.
     */
    public List<JobScheduler> getAllJobScheduler(String token) {
        return jobSchedulerMapper.getAll();
    }

    // ... (La parte correspondiente a Employee, Register y demás se encuentra en la siguiente entrega)


// ==================== PROGRAMADOR DE TRABAJOS (JOB SCHEDULER) ====================
    /**
     * Busca programadores de trabajos basados en los criterios definidos en el objeto JobSchedulerQuery.
     *
     * @param token Token de autorización.
     * @param query Objeto que contiene los criterios de búsqueda.
     * @return Lista de JobScheduler que coinciden con los criterios.
     */
    public List<JobScheduler> findJobSchedulerByQuery(String token, JobSchedulerQuery query) {
        return jobSchedulerMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza un programador de trabajos después de validar su información.
     *
     * @param token Token de autorización.
     * @param jobScheduler Objeto JobScheduler a guardar o actualizar.
     * @param method Método HTTP (POST para insertar, PUT para actualizar).
     * @throws NoDataException Si faltan datos requeridos.
     * @throws UniqueException Si se viola una restricción de unicidad.
     */
    @Transactional
    public void saveJobScheduler(String token, JobScheduler jobScheduler, String method) throws NoDataException, UniqueException {
        validateJobScheduler(token, jobScheduler, method);
        if (jobScheduler.getId() == null) {
            jobSchedulerMapper.insert(jobScheduler);
        } else {
            jobSchedulerMapper.update(jobScheduler);
        }
    }

    /**
     * Valida la información de un programador de trabajos.
     * Verifica que se especifiquen el nombre y los indicadores de cada día (lunes a domingo).
     * Además, se asegura de que el nombre sea único.
     *
     * @param token Token de autorización.
     * @param jobScheduler Objeto JobScheduler a validar.
     * @param method Método HTTP (POST o PUT).
     * @throws NoDataException Si falta información obligatoria.
     * @throws UniqueException Si ya existe otro programador de trabajos con el mismo nombre.
     */
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

    /**
     * Elimina un programador de trabajos a partir de su ID.
     * Antes de eliminar, verifica que no tenga empleados asociados.
     *
     * @param token Token de autorización.
     * @param jobSchedulerId ID del programador de trabajos a eliminar.
     * @return El objeto JobScheduler eliminado.
     * @throws NoDataException Si no se encuentra o tiene empleados asociados.
     */
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

    /**
     * Retorna los programadores de trabajos asociados a una compañía.
     *
     * @param companyId ID de la compañía.
     * @return Lista de JobScheduler asociados.
     * @throws NoDataException Si no se especifica el ID.
     */
    public List<JobScheduler> findJobSchedulersByCompanyId(Long companyId) throws NoDataException {
        if (companyId == null) {
            throw new NoDataException("El ID de la compañía es requerido.");
        }
        return jobSchedulerMapper.findByCompanyId(companyId);
    }

    /**
     * Retorna los programadores de trabajos asociados a una sucursal.
     *
     * @param branchId ID de la sucursal.
     * @return Lista de JobScheduler asociados.
     * @throws NoDataException Si no se especifica el ID.
     */
    public List<JobScheduler> findJobSchedulersByBranchId(Long branchId) throws NoDataException {
        if (branchId == null) {
            throw new NoDataException("El ID de la sucursal es requerido.");
        }
        return jobSchedulerMapper.findByBranchId(branchId);
    }

    // ==================== EMPLEADO (EMPLOYEE) ====================
    /**
     * Obtiene un empleado a partir de su ID.
     *
     * @param token Token de autorización.
     * @param employeeId ID del empleado.
     * @return Objeto Employee encontrado.
     * @throws NoDataException Si no se especifica el ID o no se encuentra el empleado.
     */
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

    /**
     * Retorna todos los empleados.
     */
    public List<Employee> getAllEmployees(String token) {
        return employeeMapper.getAll();
    }

    /**
     * Busca empleados basados en los criterios definidos en EmployeeQuery.
     */
    public List<Employee> findEmployeesByQuery(String token, EmployeeQuery query) {
        return employeeMapper.findByQuery(query);
    }

    /**
     * Retorna los empleados asociados a una compañía.
     *
     * @param token Token de autorización.
     * @param companyId ID de la compañía.
     * @return Lista de empleados.
     * @throws NoDataException Si no se especifica el ID o no se encuentran empleados.
     */
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

    /**
     * Guarda o actualiza un empleado junto con la información del usuario de la aplicación.
     * Se reciben ambos conjuntos de datos en un objeto SaveEmployeeRequest.
     * Primero se guarda el empleado y, a continuación, se guarda el AppUser asociado.
     */
    @Transactional
    public void saveEmployee(String token, Employee emplo, String method) throws NoDataException, UniqueException {
        if (emplo == null) {
            throw new NoDataException("La solicitud no contiene la información del empleado.");
        }
        validateEmployee(token, emplo, method);
        if (emplo.getId() == null) {
            employeeMapper.insert(emplo);
        } else {
            employeeMapper.update(emplo);
        }
    }

    /**
     * Valida que el empleado tenga la información obligatoria (nombre, apellido, género, comuna, sucursal, horario, etc.)
     * y que el empleado sea único (en POST, no debe existir; en PUT, el ID debe existir y ser el mismo).
     */
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

    /**
     * Elimina un empleado a partir de su ID.
     * Verifica que el empleado no tenga registros de asistencia asociados antes de eliminarlo.
     */
    public Employee deleteEmployeeById(String token, Long employeeId) throws NoDataException {
        Employee employee = employeeMapper.get(employeeId);
        if (employee == null) {
            throw new NoDataException("No se encontró el empleado con el ID especificado: " + employeeId);
        }
        if (employeeId == null) {
            throw new NoDataException("No se especifico el id del empleado: ");
        }
        RegisterQuery registerQuery = new RegisterQuery();
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setId(employeeId);
        registerQuery.setEmployee(employeeQuery);
        List<Register> registries = registerMapper.findByQuery(registerQuery);
        if (registries != null && !registries.isEmpty()) {
            throw new NoDataException("No se puede eliminar el empleado porque tiene registros asociados.");
        }
        employeeMapper.delete(employeeId);
        return employee;
    }

    // ==================== ROL (JOB TYPE) ====================
    /**
     * Obtiene un rol a partir de su ID.
     */
    public JobType getJobTypeById(String token, JobType jobType) throws NoDataException {
        if (jobType == null || jobType.getId() == null) {
            throw new NoDataException("No se especificó el ID de rol.");
        }
        JobType foundJobType = jobTypeMapper.get(jobType.getId());
        if (foundJobType == null) {
            throw new NoDataException("No se encontró la sucursal con el ID especificado: " + jobType.getId());
        }
        return foundJobType;
    }

    /**
     * Retorna todos los roles.
     */
    public List<JobType> getAllJobType(String token) {
        return jobTypeMapper.getAll();
    }

    /**
     * Busca roles basados en los criterios definidos en JobTypeQuery.
     */
    public List<JobType> findJobTypeByQuery(String token, JobTypeQuery query) {
        return jobTypeMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza un rol, validando su información.
     */
    @Transactional
    public void saveJobType(String token, JobType jobType, String method) throws NoDataException, UniqueException {
        validateJobType(token, jobType, method);
        if (jobType.getId() == null) {
            jobTypeMapper.insert(jobType);
        } else {
            jobTypeMapper.update(jobType);
        }
    }

    /**
     * Retorna los roles asociados a una compañía.
     *
     * @param companyId ID de la compañía.
     * @return Lista de JobType.
     * @throws NoDataException Si no se encuentran roles.
     */
    public List<JobType> getJobTypesByCompany(Long companyId) throws NoDataException {
        JobTypeQuery query = new JobTypeQuery();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setId(companyId);
        query.setCompany(companyQuery);
        List<JobType> jobTypes = jobTypeMapper.findByQuery(query);
        if (jobTypes == null || jobTypes.isEmpty()) {
            throw new NoDataException("No se encontraron roles para la compañía con ID: " + companyId);
        }
        return jobTypes;
    }

    /**
     * Valida la información del rol, asegurándose de que tenga nombre y compañía, y que el nombre sea único.
     */
    private void validateJobType(String token, JobType jobType, String method) throws NoDataException, UniqueException {
        if (jobType == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del rol");
        }
        if (Boolean.TRUE.equals(jobType.getActive())) {
            if (jobType.getName() == null || StringUtils.isBlank(jobType.getName())) {
                throw new NoDataException("No se especificó el campo nombre del rol");
            }
            if (jobType.getCompany() == null || jobType.getCompany().getId() == null) {
                throw new NoDataException("No se especificó el campo compañía de la sucursal");
            }
        }
        JobTypeQuery query = new JobTypeQuery();
        query.setName(jobType.getName());
        List<JobType> jobTypeDBList = jobTypeMapper.findByQuery(query);
        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            if (jobType.getId() != null) {
                throw new NoDataException("El campo ID debe ser nulo para un rol");
            }
            if (jobTypeDBList != null && !jobTypeDBList.isEmpty()) {
                throw new UniqueException("El rol ya existe");
            }
        } else if (HttpMethod.PUT.equalsIgnoreCase(method)) {
            if (jobType.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar el rol");
            }
            if (jobTypeDBList != null && !jobTypeDBList.isEmpty()) {
                for (JobType existingJobType : jobTypeDBList) {
                    if (!existingJobType.getId().equals(jobType.getId())) {
                        throw new UniqueException("El rol ya existe con el mismo nombre");
                    }
                }
            }
        }
    }

    /**
     * Elimina un rol a partir de su ID.
     * Verifica que no tenga empleados asociados antes de eliminarlo.
     */
    public JobType deleteJobTypeById(String token, Long jobTypeId) throws NoDataException {
        JobType jobType = jobTypeMapper.get(jobTypeId);
        if (jobType == null) {
            throw new NoDataException("No se encontró el rol con el ID especificado");
        }
        EmployeeQuery employeeQuery = new EmployeeQuery();
        JobTypeQuery jobTypeQuery = new JobTypeQuery();
        jobTypeQuery.setId(jobTypeId);
        employeeQuery.setJobType(jobTypeQuery);
        List<Employee> employees = findEmployeesByQuery(token, employeeQuery);
        if (employees != null && !employees.isEmpty()) {
            throw new NoDataException("No se puede eliminar el rol  porque tiene empleados asociados.");
        }
        jobTypeMapper.delete(jobTypeId);
        return jobType;
    }

    // ==================== REGISTRO (REGISTER) ====================
    /**
     * Obtiene un registro de asistencia a partir de su ID.
     */
    public Register getRegistryById(String token, Register register) throws NoDataException {
        if (register == null || register.getId() == null) {
            throw new NoDataException("No se especificó el ID del registro.");
        }
        Register foundRegister = registerMapper.get(register.getId());
        if (foundRegister == null) {
            throw new NoDataException("No se encontró el registro con el ID especificado: " + register.getId());
        }
        return foundRegister;
    }

    /**
     * Retorna todos los registros de asistencia.
     */
    public List<Register> getAllRegistry(String token) {
        return registerMapper.getAll();
    }

    /**
     * Busca registros de asistencia según los criterios definidos en RegisterQuery.
     */
    public List<Register> findRegistryByQuery(String token, RegisterQuery query) {
        return registerMapper.findByQuery(query);
    }

    /**
     * Guarda o actualiza un registro de asistencia, validando previamente la información.
     */
    @Transactional
    public void saveRegistry(String token, Register register, String method) throws NoDataException, UniqueException {
        validateRegistry(token, register, method);
        if (register.getId() == null) {
            registerMapper.insert(register);
        } else {
            registerMapper.update(register);
        }
    }

    /**
     * Valida la información del registro de asistencia, asegurándose de que los campos obligatorios (inicio del día, día, entrada y salida) estén presentes
     * y que no exista ya un registro con el mismo tiempo.
     */
    private void validateRegistry(String token, Register register, String method) throws NoDataException, UniqueException {
        if (register == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del registro");
        }
        if (Boolean.TRUE.equals(register.getActive())) {
            if (register.getStartOfTheDay() == null) {
                throw new NoDataException("No se especificó el campo inicio del día");
            }
            if (register.getDay() == null) {
                throw new NoDataException("No se especificó el campo día");
            }
            if (register.getEntry() == null) {
                throw new NoDataException("No se especificó el campo entrada");
            }
            if (register.getExit() == null) {
                throw new NoDataException("No se especificó el campo salida");
            }
        }
        RegisterQuery query = new RegisterQuery();
        query.setStartOfTheDay(register.getStartOfTheDay());
        query.setDay(register.getDay());
        query.setEntry(register.getEntry());
        query.setExit(register.getExit());
        List<Register> registerDBList = registerMapper.findByQuery(query);
        if ("POST".equalsIgnoreCase(method)) {
            if (register.getId() != null) {
                throw new NoDataException("El campo ID debe ser nulo para el registro");
            }
            if (registerDBList != null && !registerDBList.isEmpty()) {
                throw new UniqueException("El registro ya existe");
            }
        } else if ("PUT".equalsIgnoreCase(method)) {
            if (register.getId() == null) {
                throw new NoDataException("No se especificó el campo ID para actualizar el registro");
            }
            if (registerDBList != null && !registerDBList.isEmpty()) {
                for (Register existingRegister : registerDBList) {
                    if (!existingRegister.getId().equals(register.getId())) {
                        throw new UniqueException("Ya existe otro registro con el mismo tiempo");
                    }
                }
            }
        }
    }

    /**
     * Elimina un registro de asistencia a partir de su ID.
     *
     * @param token Token de autorización.
     * @param registryId ID del registro a eliminar.
     * @return El registro eliminado.
     * @throws NoDataException Si no se encuentra el registro.
     */
    public Register deleteRegistryById(String token, Long registryId) throws NoDataException {
        Register register = registerMapper.get(registryId);
        if (register == null) {
            throw new NoDataException("No se encontró el registro con el ID especificado");
        }
        RegisterQuery registerQuery = new RegisterQuery();
        registerQuery.setId(registryId);
        registerMapper.delete(registryId);
        return register;
    }

    /**
     * Retorna registros asociados a una compañía a partir del ID de la compañía.
     *
     * @param companyId ID de la compañía.
     * @return Lista de registros.
     * @throws NoDataException Si el ID es nulo o no se encuentran registros.
     */
    public List<Register> findByCompanyId(Long companyId) throws NoDataException {
        if (companyId == null) {
            throw new NoDataException("El ID de la compañía no puede ser nulo.");
        }
        List<Register> registries = registerMapper.findByCompanyId(companyId);
        if (registries == null || registries.isEmpty()) {
            throw new NoDataException("No se encontraron registros para la compañía con ID: " + companyId);
        }
        return registries;
    }

    /**
     * Retorna registros asociados a una sucursal a partir del ID de la sucursal.
     *
     * @param branchId ID de la sucursal.
     * @return Lista de registros.
     * @throws NoDataException Si el ID es nulo o no se encuentran registros.
     */
    public List<Register> findByBranchId(Long branchId) throws NoDataException {
        if (branchId == null) {
            throw new NoDataException("El ID de la sucursal no puede ser nulo.");
        }
        List<Register> registries = registerMapper.findByBranchId(branchId);
        if (registries == null || registries.isEmpty()) {
            throw new NoDataException("No se encontraron registros para la sucursal con ID: " + branchId);
        }
        return registries;
    }

    /**
     * Procesa y guarda un registro de asistencia basado en el tipo de registro.
     * Los tipos definen si se trata de entrada, inicio de break, fin de break o salida.
     *
     * @param token Token de autorización.
     * @param request Objeto SaveRegisterRequest que contiene el registro y el tipo de registro.
     * @return El registro procesado.
     * @throws NoDataException Si la solicitud o el registro están vacíos o el tipo no es válido.
     * @throws UniqueException Si se viola alguna restricción de unicidad.
     */
    public Register processRegister(String token, SaveRegisterRequest request) throws NoDataException, UniqueException {
        if (request == null || request.getRegister() == null) {
            throw new NoDataException("La solicitud de registro está vacía");
        }
        Long type = request.getTypeRegister();
        Register reg = request.getRegister();
        switch (type.intValue()) {
            case 1:
                reg.setDay(new java.sql.Date(System.currentTimeMillis()));
                reg.setStartOfTheDay(new Time(System.currentTimeMillis()));
                reg.setEntry(new Time(System.currentTimeMillis()));
                saveRegistry(token, reg, "POST");
                break;
            case 2:
                Register regBreak = getRegistryById(token, reg);
                if (regBreak == null) {
                    throw new NoDataException("Registro no encontrado para iniciar break");
                }
                regBreak.setBreakTime(new Time(System.currentTimeMillis()));
                saveRegistry(token, regBreak, "PUT");
                reg = regBreak;
                break;
            case 3:
                Register regBack = getRegistryById(token, reg);
                if (regBack == null) {
                    throw new NoDataException("Registro no encontrado para finalizar break");
                }
                regBack.setBackToWork(new Time(System.currentTimeMillis()));
                saveRegistry(token, regBack, "PUT");
                reg = regBack;
                break;
            case 4:
                Register regExit = getRegistryById(token, reg);
                if (regExit == null) {
                    throw new NoDataException("Registro no encontrado para salida");
                }
                regExit.setExit(new Time(System.currentTimeMillis()));
                saveRegistry(token, regExit, "PUT");
                reg = regExit;
                break;
            default:
                throw new NoDataException("Tipo de registro no válido: " + type);
        }
        return reg;
    }





}



