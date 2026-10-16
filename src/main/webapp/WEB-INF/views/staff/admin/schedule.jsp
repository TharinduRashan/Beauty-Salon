<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Staff Roster" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/staff" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Staff List</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Roster &amp; Shifts: <c:out value="${staff.fullName}"/></h1>
            </div>
        </div>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <!-- Add Regular Schedule Form -->
            <div class="bg-salon-50/70 p-6 rounded-3xl border border-salon-100">
                <h3 class="font-serif text-lg font-bold text-salon-900 mb-4">Set Working Shift</h3>
                <form action="${pageContext.request.contextPath}/admin/staff/${staff.id}/schedule/add" method="post" class="space-y-4">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div>
                        <label for="dayOfWeek" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Day of Week</label>
                        <select id="dayOfWeek" name="dayOfWeek" required class="w-full px-3 py-2.5 bg-white border border-salon-200 rounded-xl text-xs text-salon-900">
                            <c:forEach items="${daysOfWeek}" var="d">
                                <option value="${d}">${d}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="grid grid-cols-2 gap-2">
                        <div>
                            <label for="startTime" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Start</label>
                            <input type="time" id="startTime" name="startTime" value="09:00" required class="w-full px-3 py-2 bg-white border border-salon-200 rounded-xl text-xs text-salon-900">
                        </div>
                        <div>
                            <label for="endTime" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">End</label>
                            <input type="time" id="endTime" name="endTime" value="18:00" required class="w-full px-3 py-2 bg-white border border-salon-200 rounded-xl text-xs text-salon-900">
                        </div>
                    </div>

                    <div class="grid grid-cols-2 gap-2">
                        <div>
                            <label for="breakStart" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Break Start</label>
                            <input type="time" id="breakStart" name="breakStart" value="12:00" class="w-full px-3 py-2 bg-white border border-salon-200 rounded-xl text-xs text-salon-900">
                        </div>
                        <div>
                            <label for="breakEnd" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Break End</label>
                            <input type="time" id="breakEnd" name="breakEnd" value="13:00" class="w-full px-3 py-2 bg-white border border-salon-200 rounded-xl text-xs text-salon-900">
                        </div>
                    </div>

                    <button type="submit" class="w-full text-xs font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3 rounded-xl transition-all">
                        + Assign Shift
                    </button>
                </form>
            </div>

            <!-- Existing Schedules Table -->
            <div class="lg:col-span-2 overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm p-4">
                <table class="w-full text-left border-collapse text-xs">
                    <thead>
                        <tr class="bg-salon-50/70 border-b border-salon-100 uppercase tracking-wider text-salon-700">
                            <th class="py-3 px-4 font-semibold">Type</th>
                            <th class="py-3 px-4 font-semibold">Details</th>
                            <th class="py-3 px-4 font-semibold">Hours / Dates</th>
                            <th class="py-3 px-4 font-semibold text-right">Action</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-salon-100">
                        <c:forEach items="${schedules}" var="sc">
                            <tr class="hover:bg-salon-50/40">
                                <td class="py-3 px-4 font-semibold">${sc.type}</td>
                                <td class="py-3 px-4 font-medium text-salon-900">${sc.dayOfWeek != null ? sc.dayOfWeek : 'Leave Request'}</td>
                                <td class="py-3 px-4 text-salon-600">
                                    <c:choose>
                                        <c:when test="${sc.type == 'REGULAR'}">
                                            ${sc.startTime} - ${sc.endTime} (Break: ${sc.breakStart} - ${sc.breakEnd})
                                        </c:when>
                                        <c:otherwise>
                                            ${sc.leaveDate} to ${sc.leaveDateEnd}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="py-3 px-4 text-right">
                                    <form action="${pageContext.request.contextPath}/admin/staff/schedule/${sc.id}/cancel" method="post" class="inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="staffId" value="${staff.id}"/>
                                        <button type="submit" class="text-red-600 hover:text-red-800 font-semibold">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
