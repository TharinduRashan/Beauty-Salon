<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Users" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">User Management</h1>
            </div>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                        <th class="py-4 px-6 font-semibold">Username</th>
                        <th class="py-4 px-6 font-semibold">Full Name</th>
                        <th class="py-4 px-6 font-semibold">Email</th>
                        <th class="py-4 px-6 font-semibold">Role</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                        <th class="py-4 px-6 font-semibold text-right">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${users}" var="u">
                        <tr class="hover:bg-salon-50/40 transition-colors">
                            <td class="py-4 px-6 font-semibold text-salon-900"><c:out value="${u.username}"/></td>
                            <td class="py-4 px-6 text-salon-800"><c:out value="${u.fullName}"/></td>
                            <td class="py-4 px-6 text-salon-600"><c:out value="${u.email}"/></td>
                            <td class="py-4 px-6">
                                <form action="${pageContext.request.contextPath}/admin/users/${u.id}/role" method="post" class="inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <select name="role" onchange="this.form.submit()" class="text-xs bg-salon-50 border border-salon-200 rounded-lg px-2 py-1 font-semibold text-salon-800">
                                        <option value="CUSTOMER" ${u.role == 'CUSTOMER' ? 'selected' : ''}>CUSTOMER</option>
                                        <option value="STAFF" ${u.role == 'STAFF' ? 'selected' : ''}>STAFF</option>
                                        <option value="ADMIN" ${u.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                                    </select>
                                </form>
                            </td>
                            <td class="py-4 px-6">
                                <form action="${pageContext.request.contextPath}/admin/users/${u.id}/status" method="post" class="inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <select name="status" onchange="this.form.submit()" class="text-xs bg-salon-50 border border-salon-200 rounded-lg px-2 py-1 font-semibold
                                        ${u.status == 'ACTIVE' ? 'text-green-700' : 'text-red-700'}">
                                        <option value="ACTIVE" ${u.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                        <option value="INACTIVE" ${u.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                                        <option value="SUSPENDED" ${u.status == 'SUSPENDED' ? 'selected' : ''}>SUSPENDED</option>
                                    </select>
                                </form>
                            </td>
                            <td class="py-4 px-6 text-right">
                                <form action="${pageContext.request.contextPath}/admin/users/${u.id}/delete" method="post" class="inline" onsubmit="return confirm('Permanently remove this account?');">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-semibold">Delete</button>
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
