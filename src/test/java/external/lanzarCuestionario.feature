Feature: lanzar un cuestionario

@drop_quizz
Scenario: lanzar cuestionario profesor
 Given call read('login.feature@login_profesor')
 Given call read('createCuestionario.feature@create_quizz')
 When click("a[href='/catalogo']")
 Then waitForUrl(baseUrl + '/catalogo')