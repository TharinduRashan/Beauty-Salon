<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Appointments" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Appointments Schedule</h1>
            </div>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                        <th class="py-4 px-6 font-semibold">Client</th>
                        <th class="py-4 px-6 font-semibold">Service</th>
                        <th class="py-4 px-6 font-semibold">Specialist</th>
                        <th class="py-4 px-6 font-semibold">Date &amp; Time</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                        <th class="py-4 px-6 font-semibold text-right">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${appointments}" var="a">
                        <tr class="hover:bg-salon-50/40 transition-colors">
                            <td class="py-4 px-6 font-semibold text-salon-900"><c:out value="${a.customerName}"/></td>
                            <td class="py-4 px-6 text-salon-800"><c:out value="${a.serviceNameSnapshot}"/></td>
                            <td class="py-4 px-6 text-salon-600"><c:out value="${a.staffName}"/></td>
                            <td class="py-4 px-6 text-xs text-salon-700">
                                <div>${a.formattedDate}</div>
                                <div class="text-salon-400">${a.formattedStartTime} - ${a.formattedEndTime}</div>
                            </td>
                            <td class="py-4 px-6">
                                <form action="${pageContext.request.contextPath}/admin/appointments/${a.id}/status" method="post" class="inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <select name="status" onchange="this.form.submit()" class="text-xs bg-salon-50 border border-salon-200 rounded-lg px-2 py-1 font-semibold
                                        ${a.status == 'CONFIRMED' ? 'text-blue-700' : ''}
                                        ${a.status == 'COMPLETED' ? 'text-green-700' : ''}
                                        ${a.status == 'CANCELLED' ? 'text-red-700' : ''}">
                                        <option value="PENDING" ${a.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                        <option value="CONFIRMED" ${a.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                        <option value="COMPLETED" ${a.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                                        <option value="CANCELLED" ${a.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                        <option value="NO_SHOW" ${a.status == 'NO_SHOW' ? 'selected' : ''}>NO_SHOW</option>
                                    </select>
                                </form>
                            </td>
                            <td class="py-4 px-6 text-right">
                                <form action="${pageContext.request.contextPath}/admin/appointments/${a.id}/cancel" method="post" class="inline" onsubmit="return confirm('Cancel this appointment?');">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-semibold">Cancel</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
