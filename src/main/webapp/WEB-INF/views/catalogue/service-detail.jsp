<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${service.name}" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <a href="${pageContext.request.contextPath}/services" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">
                &larr; Back to Catalogue
            </a>
            <span class="px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider bg-salon-50 text-salon-700 border border-salon-100">
                <c:out value="${service.category}"/>
            </span>
            <h1 class="text-3xl font-serif font-bold text-salon-900 mt-2"><c:out value="${service.name}"/></h1>
        </div>

        <div class="bg-salon-50/70 p-8 rounded-3xl border border-salon-100 shadow-sm space-y-6">
            <p class="text-sm text-salon-700 leading-relaxed font-light">
                <c:out value="${service.description}"/>
            </p>

            <div class="grid grid-cols-2 gap-4 pt-4 border-t border-salon-200/60 text-sm">
                <div>
                    <span class="text-xs text-salon-400 block font-light">Duration</span>
                    <span class="text-base font-bold text-salon-900">${service.durationFormatted}</span>
                </div>
                <div>
                    <span class="text-xs text-salon-400 block font-light">Treatment Fee</span>
                    <span class="text-xl font-serif font-bold text-salon-900">Rs. ${service.price}</span>
                </div>
            </div>

            <div class="pt-4">
                <a href="${pageContext.request.contextPath}/customer/appointments/book?serviceId=${service.id}"
                   class="inline-block text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-8 py-3.5 rounded-full shadow-md transition-all">
                    Book This Treatment
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
