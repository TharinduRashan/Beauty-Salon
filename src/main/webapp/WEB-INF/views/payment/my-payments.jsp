<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Payments" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Billing History</span>
            <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1">Payment Records</h1>
        </div>

        <c:choose>
            <c:when test="${empty payments}">
                <div class="text-center py-20 bg-salon-50 rounded-3xl border border-dashed border-salon-200">
                    <i class="fa-solid fa-receipt text-salon-300 text-4xl mb-3"></i>
                    <h3 class="font-serif text-lg font-bold text-salon-800">No Payment Records Found</h3>
                    <p class="text-xs text-salon-500 mt-1">Transactions will appear here once reservations are completed.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
                    <table class="w-full text-left border-collapse text-sm">
                        <thead>
                            <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                                <th class="py-4 px-6 font-semibold">Ref Code</th>
                                <th class="py-4 px-6 font-semibold">Service</th>
                                <th class="py-4 px-6 font-semibold">Amount</th>
                                <th class="py-4 px-6 font-semibold">Method</th>
                                <th class="py-4 px-6 font-semibold">Status</th>
                                <th class="py-4 px-6 font-semibold">Date</th>
                                <th class="py-4 px-6 font-semibold text-right">Receipt</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-salon-100">
                            <c:forEach items="${payments}" var="p">
                                <tr class="hover:bg-salon-50/40 transition-colors">
                                    <td class="py-4 px-6 font-mono text-xs text-salon-700 font-semibold">${p.transactionRef != '' ? p.transactionRef : 'PENDING'}</td>
                                    <td class="py-4 px-6 font-medium text-salon-900"><c:out value="${p.serviceNameSnapshot}"/></td>
                                    <td class="py-4 px-6 font-bold text-salon-900">Rs. ${p.total}</td>
                                    <td class="py-4 px-6 text-salon-600">${p.paymentMethod}</td>
                                    <td class="py-4 px-6">
                                        <span class="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold
                                            ${p.paymentStatus == 'PAID' ? 'bg-green-50 text-green-700' : 'bg-amber-50 text-amber-700'}">
                                            ${p.paymentStatus}
                                        </span>
                                    </td>
                                    <td class="py-4 px-6 text-xs text-salon-500">${p.formattedCreatedAt}</td>
                                    <td class="py-4 px-6 text-right">
                                        <c:if test="${p.paid}">
                                            <a href="${pageContext.request.contextPath}/customer/payments/receipt/${p.id}" class="text-xs font-semibold text-salon-600 hover:text-salon-800">
                                                View Receipt &rarr;
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
