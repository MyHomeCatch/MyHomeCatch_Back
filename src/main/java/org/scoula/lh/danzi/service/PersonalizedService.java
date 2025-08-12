package org.scoula.lh.danzi.service;

import org.scoula.lh.danzi.dto.http.PersonalizedCardDTO;

public interface PersonalizedService {
    public PersonalizedCardDTO getOrCreatePersonalCard(int danziId, int userId);
}
