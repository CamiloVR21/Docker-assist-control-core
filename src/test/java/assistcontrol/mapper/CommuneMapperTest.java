package assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.mapper.CommuneMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommuneMapperTest {

    @Mock
    private CommuneMapper communeMapper;

    @InjectMocks
    private CommuneMapperTest communeMapperTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByQueryReturnsEmptyList() {
        CommuneQuery query = new CommuneQuery();
        when(communeMapper.findByQuery(query)).thenReturn(Collections.emptyList());

        List<Commune> result = communeMapper.findByQuery(query);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getByQueryReturnsCommune() {
        CommuneQuery query = new CommuneQuery();
        Commune commune = new Commune();
        when(communeMapper.getByQuery(query)).thenReturn(commune);

        Commune result = communeMapper.getByQuery(query);

        assertEquals(commune, result);
    }

    @Test
    void findByQueryReturnsCommuneList() {
        CommuneQuery query = new CommuneQuery();
        Commune commune = new Commune();
        List<Commune> communeList = List.of(commune);
        when(communeMapper.findByQuery(query)).thenReturn(communeList);

        List<Commune> result = communeMapper.findByQuery(query);

        assertEquals(communeList, result);
    }
}