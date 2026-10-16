<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Payment Audit Reports" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Financial Reports &amp; Audit</h1>
            </div>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                        <th class="py-4 px-6 font-semibold">Ref Code</th>
                        <th class="py-4 px-6 font-semibold">Client</th>
                        <th class="py-4 px-6 font-semibold">Service</th>
                        <th class="py-4 px-6 font-semibold">Total Paid</th>
                        <th class="py-4 px-6 font-semibold">Method</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                        <th class="py-4 px-6 font-semibold text-right">Refund Action</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${payments}" var="p">
                        <tr class="hover:bg-salon-50/40 transition-colors">
                            <td class="py-4 px-6 font-mono text-xs font-semibold text-salon-700">${p.transactionRef}</td>
                            <td class="py-4 px-6 font-semibold text-salon-900"><c:out value="${p.customerName}"/></td>
                            <td class="py-4 px-6 text-salon-800"><c:out value="${p.serviceNameSnapshot}"/></td>
                            <td class="py-4 px-6 font-bold text-salon-900">Rs. ${p.total}</td>
                            <td class="py-4 px-6 text-salon-600">${p.paymentMethod}</td>
                            <td class="py-4 px-6">
                                <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold
                                    ${p.paymentStatus == 'PAID' ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}">
                                    ${p.paymentStatus}
                                </span>
                            </td>
                            <td class="py-4 px-6 text-right">
                                <c:if test="${p.paymentStatus == 'PAID'}">
                                    <form action="${pageContext.request.contextPath}/admin/payments/${p.id}/refund" method="post" class="inline" onsubmit="return confirm('Authorize refund for this payment?');">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <input type="hidden" name="reason" value="Administrative refund"/>
                                        <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-semibold">Issue Refund</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
