<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="System Error" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="flex-grow flex items-center justify-center py-20 px-4 text-center">
    <div class="max-w-md bg-white p-10 rounded-3xl border border-salon-100 shadow-sm">
        <div class="w-16 h-16 rounded-full bg-red-50 text-red-600 flex items-center justify-center mx-auto mb-4 text-2xl">
            <i class="fa-solid fa-triangle-exclamation"></i>
        </div>
        <h1 class="font-serif text-3xl font-bold text-salon-900 mb-2">500 Server Error</h1>
        <p class="text-xs sm:text-sm text-salon-600 mb-6 leading-relaxed">
            <c:out value="${errorMessage != null ? errorMessage : 'An unexpected disturbance occurred while rendering this service.'}"/>
        </p>
        <a href="${pageContext.request.contextPath}/" class="text-xs font-semibold text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all">
            Return to Sanctuary
        </a>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
