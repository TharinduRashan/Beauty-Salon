<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Appointments" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 pb-4 border-b border-salon-100">
            <div>
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">History &amp; Schedule</span>
                <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1">My Appointments</h1>
            </div>
            <a href="${pageContext.request.contextPath}/customer/appointments/book"
               class="mt-4 sm:mt-0 text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all shadow-sm">
                + Book New
            </a>
        </div>

        <c:choose>
            <c:when test="${empty appointments}">
                <div class="text-center py-20 bg-salon-50 rounded-3xl border border-dashed border-salon-200">
                    <i class="fa-regular fa-calendar-xmark text-salon-300 text-4xl mb-3"></i>
                    <h3 class="font-serif text-lg font-bold text-salon-800">No Appointments Reserved</h3>
                    <p class="text-xs text-salon-500 mt-1">Ready for a refreshing beauty experience?</p>
                    <a href="${pageContext.request.contextPath}/customer/appointments/book" class="inline-block mt-4 text-xs font-semibold text-salon-600 underline">Schedule one now</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
                            <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                                <th class="py-4 px-6 font-semibold">Service</th>
                                <th class="py-4 px-6 font-semibold">Specialist</th>
                                <th class="py-4 px-6 font-semibold">Date &amp; Time</th>
                                <th class="py-4 px-6 font-semibold">Fee</th>
                                <th class="py-4 px-6 font-semibold">Status</th>
                                <th class="py-4 px-6 font-semibold text-right">Actions</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-salon-100">
                            <c:forEach items="${appointments}" var="a">
                                <tr class="hover:bg-salon-50/40 transition-colors">
                                    <td class="py-4 px-6 font-medium text-salon-900">
                                        <a href="${pageContext.request.contextPath}/customer/appointments/${a.id}" class="hover:text-salon-600">
                                            <c:out value="${a.serviceNameSnapshot}"/>
                                        </a>
                                    </td>
                                    <td class="py-4 px-6 text-salon-600"><c:out value="${a.staffName}"/></td>
                                    <td class="py-4 px-6 text-salon-700">
                                        <div>${a.formattedDate}</div>
                                        <div class="text-xs text-salon-400 font-light">${a.formattedStartTime} - ${a.formattedEndTime}</div>
                                    </td>
                                    <td class="py-4 px-6 font-semibold text-salon-900">Rs. ${a.priceSnapshot}</td>
                                    <td class="py-4 px-6">
                                        <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold
                                            ${a.status == 'CONFIRMED' ? 'bg-blue-50 text-blue-700' : ''}
                                            ${a.status == 'COMPLETED' ? 'bg-green-50 text-green-700' : ''}
                                            ${a.status == 'CANCELLED' ? 'bg-red-50 text-red-700' : ''}
                                            ${a.status == 'PENDING' ? 'bg-amber-50 text-amber-700' : ''}">
                                            ${a.status}
                                        </span>
                                    </td>
                                    <td class="py-4 px-6 text-right space-x-2">
                                        <c:if test="${a.status == 'CONFIRMED'}">
                                            <a href="${pageContext.request.contextPath}/customer/payments/checkout/${a.id}"
                                               class="inline-block text-xs font-semibold text-salon-600 hover:text-salon-800 bg-salon-50 px-3 py-1.5 rounded-full border border-salon-200">
                                                Pay Now
                                            </a>
                                        </c:if>
                                        <c:if test="${a.cancellable}">
                                            <form action="${pageContext.request.contextPath}/customer/appointments/${a.id}/cancel" method="post" class="inline" onsubmit="return confirm('Cancel this appointment?');">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-semibold px-2 py-1">Cancel</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${a.status == 'COMPLETED'}">
                                            <a href="${pageContext.request.contextPath}/customer/feedback/submit?appointmentId=${a.id}"
                                               class="inline-block text-xs font-semibold text-amber-600 hover:text-amber-800">
                                                ★ Review
                                            </a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
