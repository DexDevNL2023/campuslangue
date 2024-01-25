package net.ktccenter.campusApi.controller.state;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dto.state.StateReponse;
import net.ktccenter.campusApi.service.state.StateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/state")
@RestController
@Slf4j
@CrossOrigin("*")
public class StateController {
  private final StateService stateService;

  public StateController(StateService stateService) {
      this.stateService = stateService;
  }

  @GetMapping
  public StateReponse getAllState() {
    return stateService.getAllState();
  }
}
