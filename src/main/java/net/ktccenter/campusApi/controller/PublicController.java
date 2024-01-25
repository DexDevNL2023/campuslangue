package net.ktccenter.campusApi.controller;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.enums.*;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/public")
@RestController
@Slf4j
@CrossOrigin("*")
public class PublicController {

  //Ennumerateur
  @GetMapping(value = "/enums/appreciations-note", produces = MediaTypes.HAL_JSON_VALUE)
  public List<EnumValue> getAppreciationNotes() {
    return AppreciationNote.orderedValues.stream()
      .map(e -> new EnumValue(e.name(), e.getValue()))
      .collect(Collectors.toList());
  }

  @GetMapping(value = "/enums/e-roles", produces = MediaTypes.HAL_JSON_VALUE)
  public List<EnumValue> getERoles() {
    return ERole.orderedValues.stream()
      .map(e -> new EnumValue(e.name(), e.getValue()))
      .collect(Collectors.toList());
  }

  @GetMapping(value = "/enums/jours", produces = MediaTypes.HAL_JSON_VALUE)
  public List<EnumValue> getJours() {
    return Jour.orderedValues.stream()
      .map(e -> new EnumValue(e.name(), e.getValue()))
      .collect(Collectors.toList());
  }

  @GetMapping(value = "/enums/sexes", produces = MediaTypes.HAL_JSON_VALUE)
  public List<EnumValue> getSexes() {
    return Sexe.orderedValues.stream()
      .map(e -> new EnumValue(e.name(), e.getValue()))
      .collect(Collectors.toList());
  }

  @GetMapping(value = "/enums/types-user", produces = MediaTypes.HAL_JSON_VALUE)
  public List<EnumValue> getTypeUsers() {
    return TypeUser.orderedValues.stream()
            .map(e -> new EnumValue(e.name(), e.getValue()))
            .collect(Collectors.toList());
  }
}
