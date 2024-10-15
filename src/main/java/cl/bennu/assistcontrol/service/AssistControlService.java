package cl.bennu.assistcontrol.service;

import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.mapper.CommuneMapper;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HttpMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@ApplicationScoped
public class AssistControlService {

    private @Inject CommuneMapper communeMapper;

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
        if (commune.getName() == null || StringUtils.isBlank(commune.getName()))
            throw new NoDataException("No se especificó el campo nombre");
        if (commune.getCityId() == null) throw new NoDataException("No se especificó el campo ciudad");

        CommuneQuery query = new CommuneQuery();
        query.setName(commune.getName());
        Commune communeDB = communeMapper.getByQuery(query);

        if (HttpMethod.POST.equalsIgnoreCase(method)) {
            // validaciones especificas de insert
            if (commune.getId() != null) throw new NoDataException("El campo id debe ser nulo");
            if (communeDB != null) throw new UniqueException("El tramo de riesgo ya existe");
        } else {
            // validaciones especificas de update
            if (commune.getId() == null) throw new NoDataException("No se especificó el campo id");

            if (!communeDB.getId().equals(commune.getId()))
                throw new UniqueException("El tramo de riesgo ya existe");
        }
    }

    public void deleteCommune(String token, Long communeId) {
        communeMapper.delete(communeId);
    }

}
