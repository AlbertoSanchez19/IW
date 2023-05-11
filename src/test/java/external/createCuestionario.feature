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
    #Then waitForUrl(baseUrl + '/cuestionario/\\d+/verpreguntas')
    When click('#nuevap')
    #Then waitForUrl(baseUrl + '/cuestionario/(\\d+)/(\\d+)/crearpregunta')
    #* def urlMatch = karate.match(driver.url, baseUrl + '/cuestionario/{idCuestionario}/{idPregunta}/crearpregunta')
    #* def idCuestionario = urlMatch.pathParams.idCuestionario
    #* def idPregunta = urlMatch.pathParams.idPregunta

#
# Profesor crea cuestionario con 3 preguntas de V/F, 
#  profesor pide enlace para mostrar a alumnos <-- url para realizarlo
#  
# Sale profesor, entra alumno
#  entra con código
#
# Ahora hay un alumno en el evento 
