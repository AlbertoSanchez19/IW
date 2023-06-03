Feature: crear un cuestionario

@login_cabecera
Scenario: crear cuestionario correcto
    Given call read('login.feature@login_profesor')
    When click("#cuestionariocabecera")
    Then waitForUrl(baseUrl + '/cuestionario/crear/opciones')
    And delay(1000)



