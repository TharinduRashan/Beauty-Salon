<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Manage Packages" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Package Bundles</h1>
            </div>
            <a href="${pageContext.request.contextPath}/admin/packages/new" class="text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-5 py-2.5 rounded-full">
                + New Package
            </a>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                        <th class="py-4 px-6 font-semibold">Name</th>
                        <th class="py-4 px-6 font-semibold">Price</th>
                        <th class="py-4 px-6 font-semibold">Discount</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                        <th class="py-4 px-6 font-semibold text-right">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${packages}" var="p">
                        <tr class="hover:bg-salon-50/40 transition-colors">
                            <td class="py-4 px-6 font-semibold text-salon-900"><c:out value="${p.name}"/></td>
                            <td class="py-4 px-6 font-semibold text-salon-900">Rs. ${p.totalPrice}</td>
                            <td class="py-4 px-6 text-salon-600">${p.discountPercent}%</td>
                            <td class="py-4 px-6">
                                <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold ${p.active ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}">
                                    ${p.status}
                                </span>
                            </td>
                            <td class="py-4 px-6 text-right space-x-2">
                                <a href="${pageContext.request.contextPath}/admin/packages/${p.id}/edit" class="text-xs font-semibold text-salon-600 hover:text-salon-800">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/packages/${p.id}/toggle" method="post" class="inline">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="text-xs text-salon-500 hover:text-salon-800 font-semibold">Toggle</button>
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
