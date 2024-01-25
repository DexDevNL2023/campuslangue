package net.ktccenter.campusApi.service.state;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dto.state.StateReponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class StateService {

  public StateReponse getAllState() {
    StateReponse stateResponse = new StateReponse();
    return stateResponse;
  }
}
