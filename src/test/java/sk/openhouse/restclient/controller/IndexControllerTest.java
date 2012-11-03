package sk.openhouse.restclient.controller;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sk.openhouse.restclient.form.HttpMethod;
import sk.openhouse.restclient.form.HttpMethodEditor;
import sk.openhouse.restclient.form.HttpRequest;
import sk.openhouse.restclient.service.RestService;

public class IndexControllerTest {

    @Mock
    private RestService restService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private WebDataBinder webDataBinder;

    private IndexController indexController;

    @BeforeMethod
    public void beforeMethod() {

        MockitoAnnotations.initMocks(this);
        indexController = new IndexController();
        indexController.setRestService(restService);
    }

    @Test
    public void testInitBinder() {

        indexController.initBinder(webDataBinder);
        Mockito.verify(webDataBinder, Mockito.times(1)).registerCustomEditor(
                Mockito.eq(HttpMethod.class), Mockito.any(HttpMethodEditor.class));
    }

    @Test
    public void testGetHttpRequest() {
        Assert.assertNotNull(indexController.getHttpRequest());
    }

    @Test
    public void testGetHandler() {

        ModelAndView mav = indexController.getHandler();
        Assert.assertEquals(mav.getViewName(), "index");
        Assert.assertEquals(mav.getModelMap().get("httpMethods"), HttpMethod.values());
    }

    @Test
    public void testPostHandler() {

        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView mav = indexController.postHandler(null, bindingResult);
        Assert.assertEquals(mav.getViewName(), "index");
        Assert.assertEquals(mav.getModelMap().get("httpMethods"), HttpMethod.values());

        Mockito.verify(restService, Mockito.times(1)).sendRequest(Mockito.any(HttpRequest.class));
    }

    @Test
    public void testPostHandlerErrors() {

        Mockito.when(bindingResult.hasErrors()).thenReturn(true);

        ModelAndView mav = indexController.postHandler(null, bindingResult);
        Assert.assertEquals(mav.getViewName(), "index");
        Assert.assertEquals(mav.getModelMap().get("httpMethods"), HttpMethod.values());

        Mockito.verify(restService, Mockito.times(0)).sendRequest(Mockito.any(HttpRequest.class));
    }
}
