<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>rest client</title>
        <link rel="stylesheet" type="text/css" href="resources/css/screen.css" />

        <script type="text/javascript">
        var _gaq = _gaq || [];
        _gaq.push(['_setAccount', 'UA-35270520-1']);
        _gaq.push(['_trackPageview']);

        (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
        })();
        </script>
    </head>
    <body>
        <div id="nav">
            <div class="content">
                <h1>Rest-Client</h1>
            </div><!-- .content -->
        </div><!-- #nav -->

        <div class="content">
            <form method="post" action="">
                <div class="box">

                    <spring:bind path="httpRequest.httpMethod">
	                    <label for="${status.expression}">Method</label>
	                    <select id="${status.expression}" name="${status.expression}">
	                       <c:forEach items="${httpMethods}" var="item" varStatus="loop">
	                           <option value="${item}"<c:if test="${item == status.value}"> selected="selected"</c:if>>${item}</option>
	                       </c:forEach>
	                    </select>
                    </spring:bind>

                    <spring:bind path="httpRequest.url">
                        <div style="float: right;">
                            <label for="${status.expression}"<c:if test="${status.error}"> class="error"</c:if>>Url</label>
                            <input type="text" style="width: 450px;" id="${status.expression}" name="${status.expression}" value="${status.value}" />
                            <input type="submit" value="send" />
                        </div>
                    </spring:bind>
                </div><!-- .box -->

                <div class="box">
                    <spring:bind path="httpRequest.headers">
                        <label for="${status.expression}">Headers</label>
                        <span class="error">
                           <c:if test="${status.error}">
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </span>
                        <br />
                        <textarea id="${status.expression}" name="${status.expression}">${status.value}</textarea>
                    </spring:bind>

                    <br />
                    <spring:bind path="httpRequest.body">
	                    <label for="${status.expression}">Body</label>
	                    <span class="error">
                           <c:if test="${status.error}">
                                <c:forEach items="${status.errorMessages}" var="error">
                                    <c:out value="${error}"/>
                                </c:forEach>
                            </c:if>
                        </span>
	                    <br />
	                    <textarea id="${status.expression}" name="${status.expression}">${status.value}</textarea>
                    </spring:bind>
                </div><!-- .box -->
            </form>

            <c:if test="${httpResponse != null}">
                <h2>Status Line:</h2>
                <div class="box">
                    <code>${httpResponse.statusLine}</code>
                </div><!-- .box -->

                <h2>Response Headers:</h2>
                <div class="box">
                    <code>
                        <c:forEach items="${httpResponse.headers}" var="item" varStatus="loop">
                            <b>${item.name}</b> ${item.value}<br />
                        </c:forEach>
                    </code>
                </div><!-- .box -->

                <h2>Response Body:</h2>
                <div class="box">
                    <code><c:out value="${httpResponse.responseBody}"/></code>
                </div><!-- .box -->
            </c:if>
        </div><!-- .content -->
    </body>
</html>