
package sk.openhouse.restclient.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sk.openhouse.restclient.form.HttpMethod;
import sk.openhouse.restclient.form.HttpMethodEditor;
import sk.openhouse.restclient.form.HttpRequest;
import sk.openhouse.restclient.model.HttpResponseModel;
import sk.openhouse.restclient.service.RestService;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private RestService restService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(HttpMethod.class, new HttpMethodEditor());
    }

    @ModelAttribute
    public HttpRequest getHttpRequest() {
        return new HttpRequest();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getHandler() {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("httpMethods", HttpMethod.values());

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView postHandler(@Valid HttpRequest httpRequest, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("httpMethods", HttpMethod.values());

        if (!bindingResult.hasErrors()) {
            HttpResponseModel httpResponse = restService.sendRequest(httpRequest);
            mav.addObject("httpResponse", httpResponse);
        }
        return mav;
    }

    /* used for unit testing */
    public void setRestService(RestService restService) {
        this.restService = restService;
    }
}
