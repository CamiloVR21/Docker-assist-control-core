package assistcontrol.service;

import cl.bennu.assistcontrol.domain.*;
import cl.bennu.assistcontrol.domain.query.*;
import cl.bennu.assistcontrol.mapper.*;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssistControlServiceTest {

    @Mock
    private CommuneMapper communeMapper;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private BranchMapper branchMapper;

    @Mock
    private RegionMapper regionMapper;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private AssistControlService assistControlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // COMMUNE TESTS *********************************************************************************************
    @Test
    void getCommuneByIdThrowsExceptionWhenIdIsNull() {
        Commune commune = new Commune();
        assertThrows(NoDataException.class, () -> assistControlService.getCommuneById("token", commune));
    }

    @Test
    void getCommuneByIdThrowsExceptionWhenCommuneNotFound() {
        Commune commune = new Commune();
        commune.setId(1L);
        when(communeMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getCommuneById("token", commune));
    }

    @Test
    void getCommuneByIdReturnsCommuneWhenFound() throws NoDataException {
        Commune commune = new Commune();
        commune.setId(1L);
        when(communeMapper.get(1L)).thenReturn(commune);

        Commune result = assistControlService.getCommuneById("token", commune);
        assertEquals(commune, result);
    }

    // COMPANY TESTS *********************************************************************************************
    @Test
    void getCompanyByIdThrowsExceptionWhenIdIsNull() {
        Company company = new Company();
        assertThrows(NoDataException.class, () -> assistControlService.getCompanyById("token", company));
    }

    @Test
    void getCompanyByIdThrowsExceptionWhenCompanyNotFound() {
        Company company = new Company();
        company.setId(1L);
        when(companyMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getCompanyById("token", company));
    }

    @Test
    void getCompanyByIdReturnsCompanyWhenFound() throws NoDataException {
        Company company = new Company();
        company.setId(1L);
        when(companyMapper.get(1L)).thenReturn(company);

        Company result = assistControlService.getCompanyById("token", company);
        assertEquals(company, result);
    }

    // CITY TESTS *********************************************************************************************
    @Test
    void getCityByIdThrowsExceptionWhenIdIsNull() {
        City city = new City();
        assertThrows(NoDataException.class, () -> assistControlService.getCityById("token", city));
    }

    @Test
    void getCityByIdThrowsExceptionWhenCityNotFound() {
        City city = new City();
        city.setId(1L);
        when(cityMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getCityById("token", city));
    }

    @Test
    void getCityByIdReturnsCityWhenFound() throws NoDataException {
        City city = new City();
        city.setId(1L);
        when(cityMapper.get(1L)).thenReturn(city);

        City result = assistControlService.getCityById("token", city);
        assertEquals(city, result);
    }

    // COUNTRY TESTS *********************************************************************************************
    @Test
    void getCountryByIdThrowsExceptionWhenIdIsNull() {
        Country country = new Country();
        assertThrows(NoDataException.class, () -> assistControlService.getCountryById("token", country));
    }

    @Test
    void getCountryByIdThrowsExceptionWhenCountryNotFound() {
        Country country = new Country();
        country.setId(1L);
        when(countryMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getCountryById("token", country));
    }

    @Test
    void getCountryByIdReturnsCountryWhenFound() throws NoDataException {
        Country country = new Country();
        country.setId(1L);
        when(countryMapper.get(1L)).thenReturn(country);

        Country result = assistControlService.getCountryById("token", country);
        assertEquals(country, result);
    }

    // BRANCH TESTS *********************************************************************************************
    @Test
    void getBranchByIdThrowsExceptionWhenIdIsNull() {
        Branch branch = new Branch();
        assertThrows(NoDataException.class, () -> assistControlService.getBranchById("token", branch));
    }

    @Test
    void getBranchByIdThrowsExceptionWhenBranchNotFound() {
        Branch branch = new Branch();
        branch.setId(1L);
        when(branchMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getBranchById("token", branch));
    }

    @Test
    void getBranchByIdReturnsBranchWhenFound() throws NoDataException {
        Branch branch = new Branch();
        branch.setId(1L);
        when(branchMapper.get(1L)).thenReturn(branch);

        Branch result = assistControlService.getBranchById("token", branch);
        assertEquals(branch, result);
    }

    // REGION TESTS *********************************************************************************************
    @Test
    void getRegionByIdReturnsRegion() throws NoDataException {
        Region region = new Region();
        region.setId(1L);
        when(regionMapper.get(1L)).thenReturn(region);

        Region result = assistControlService.getRegionById("token", region);
        assertEquals(region, result);
    }

    @Test
    void getRegionByIdThrowsExceptionWhenIdIsNull() {
        Region region = new Region();
        assertThrows(NoDataException.class, () -> assistControlService.getRegionById("token", region));
    }

    @Test
    void getRegionByIdThrowsExceptionWhenRegionNotFound() {
        Region region = new Region();
        region.setId(1L);
        when(regionMapper.get(1L)).thenReturn(null);

        assertThrows(NoDataException.class, () -> assistControlService.getRegionById("token", region));
    }

}
