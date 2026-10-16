<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Roster" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/staff/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">My Working Roster &amp; Shifts</h1>
            </div>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm p-4">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 uppercase tracking-wider text-salon-700 text-xs">
                        <th class="py-4 px-6 font-semibold">Day / Details</th>
                        <th class="py-4 px-6 font-semibold">Shift Hours</th>
                        <th class="py-4 px-6 font-semibold">Break Window</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${schedules}" var="sc">
                        <tr class="hover:bg-salon-50/40">
                            <td class="py-4 px-6 font-semibold text-salon-900">
                                ${sc.dayOfWeek != null ? sc.dayOfWeek : sc.type}
                            </td>
                            <td class="py-4 px-6 text-salon-700 font-medium">
                                <c:choose>
                                    <c:when test="${sc.type == 'REGULAR'}">${sc.startTime} - ${sc.endTime}</c:when>
                                    <c:otherwise>${sc.leaveDate} to ${sc.leaveDateEnd}</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="py-4 px-6 text-salon-600">
                                ${sc.breakStart != null ? sc.breakStart.toString().concat(' - ').concat(sc.breakEnd.toString()) : 'None'}
                            </td>
                            <td class="py-4 px-6">
                                <span class="px-2.5 py-1 rounded-full text-xs font-semibold ${sc.active ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}">
                                    ${sc.status}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
