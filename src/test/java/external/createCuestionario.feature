Feature: crear un cuestionario

@create_quizz
Scenario: crear cuestionario correcto
    Given call read('login.feature@login_profesor')
    When click("a[href='/cuestionario/crear/opciones']")
    Then waitForUrl(baseUrl + '/cuestionario/crear/opciones')
    When click("a[href='/cuestionario/crear']")
    And waitForUrl(baseUrl + '/cuestionario/crear')
    And input('#titulo_cuestionario', 'Titulo del cuestionario')
    And input('#descripcion_cuestionario', 'Descripcion del cuestionario')
    When submit().click("#submit_cuestionario")
    * def idCuestionario = script("location.href.replace(/.*cuestionario\\//, '').replace(/\\/verpreguntas.*/, '')")
    * def urlCuestionario = script("location.href.replace(/verpreguntas.*/, '')")
    And click('#nuevap')
    And select('#miSelect', 'VF')
    And input('#tituloVF', 'Titulo de la pregunta 1')
    And click('#bF')
    And input('#expVF', 'Explicacion a la pregunta 1')
    When click('#sigVF')
    Then waitForUrl(urlCuestionario + 'verpreguntas')

    And click('#nuevap')
    And select('#miSelect', 'DE')
    And input('#tituloDE', 'Titulo de la pregunta 2')
    And input('#explicacionDE', 'Explicacion a la pregunta 2')
    And input('#pistaDE', 'Pista a la pregunta 2')
    When click('#sigDE')
    Then waitForUrl(urlCuestionario + 'verpreguntas')

    And click('#nuevap')
    And select('#miSelect', 'OP')
    And input('#tituloOP', 'Titulo de la pregunta 3')
    And click('#aOP')
    And input('#respuestaOPA', 'Opcion A')
    And input('#respuestaOPB', 'Opcion B')
    And input('#respuestaOPC', 'Opcion C')
    And input('#respuestaOPD', 'Opcion D')
    And input('#expOP', 'Explicacion a la pregunta 3')
    When click('#sigOP')
    Then waitForUrl(urlCuestionario + 'verpreguntas')

    And click('#finCrear')

    When click("a[href='/catalogo']")
    Then waitForUrl(baseUrl + '/catalogo')
    And click('#lanzar' + idCuestionario)
    And click('#lanzar-btn')
    And delay(1000)



