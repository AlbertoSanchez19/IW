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
    And select('#miSelect', 'VF')
    And input('#tituloVF', 'Titulo de la pregunta 2')
    And click('#bV')
    And input('#expVF', 'Explicacion a la pregunta 2')
    When click('#sigVF')
    Then waitForUrl(urlCuestionario + 'verpreguntas')

    And click('#nuevap')
    And select('#miSelect', 'VF')
    And input('#tituloVF', 'Titulo de la pregunta 3')
    And click('#bV')
    And input('#expVF', 'Explicacion a la pregunta 3')
    When click('#sigVF')
    Then waitForUrl(urlCuestionario + 'verpreguntas')

    And click('#finCrear')

    When click("a[href='/catalogo']")
    Then waitForUrl(baseUrl + '/catalogo')
    And click('#lanzar' + idCuestionario)
    And click('#lanzar-btn')
    And delay(1000)

    # Falta quizás que el profesor lance el cuestionario

#
# Profesor crea cuestionario con 3 preguntas de V/F, 
#  profesor pide enlace para mostrar a alumnos <-- url para realizarlo
#  
# Sale profesor, entra alumno
#  entra con código
#
# Ahora hay un alumno en el evento 
