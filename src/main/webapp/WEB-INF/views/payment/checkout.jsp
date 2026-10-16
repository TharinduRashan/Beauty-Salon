<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Payment Checkout" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-8">
            <span class="w-12 h-12 rounded-2xl bg-salon-50 text-salon-600 flex items-center justify-center mx-auto mb-4 text-xl">
                <i class="fa-solid fa-credit-card"></i>
            </span>
            <h1 class="text-3xl font-serif font-bold text-salon-900">Simulated Checkout</h1>
            <p class="text-xs text-salon-500 mt-2">Safe academic simulation. No real financial credentials required.</p>
        </div>

        <div class="bg-salon-50/70 rounded-3xl p-8 border border-salon-100 shadow-sm space-y-6">
            <!-- Invoice Details -->
            <div class="bg-white p-6 rounded-2xl border border-salon-100 space-y-3">
                <h3 class="font-serif text-base font-bold text-salon-900 border-b border-salon-100 pb-3">Billing Summary</h3>
                <div class="flex justify-between text-xs text-salon-600">
                    <span>Treatment Service:</span>
                    <span class="font-semibold text-salon-900"><c:out value="${payment.serviceNameSnapshot}"/></span>
                </div>
                <div class="flex justify-between text-xs text-salon-600">
                    <span>Subtotal:</span>
                    <span>Rs. ${payment.amount}</span>
                </div>
                <div class="flex justify-between text-xs text-salon-600">
                    <span>VAT / Taxes (10%):</span>
                    <span>Rs. ${payment.tax}</span>
                </div>
                <div class="border-t border-salon-100 pt-3 flex justify-between text-sm font-bold text-salon-900">
                    <span>Total Amount Payable:</span>
                    <span class="text-base text-salon-600">Rs. ${payment.total}</span>
                </div>
            </div>

            <!-- Payment Method Form -->
            <form action="${pageContext.request.contextPath}/customer/payments/checkout/${payment.id}/process" method="post" class="space-y-6">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div>
                    <label class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-3">Select Payment Method</label>
                    <div class="grid grid-cols-3 gap-3">
                        <c:forEach items="${methods}" var="m">
                            <label class="cursor-pointer">
                                <input type="radio" name="paymentMethod" value="${m}" ${m == 'CASH' ? 'checked' : ''} class="peer sr-only">
                                <div class="p-4 text-center rounded-2xl bg-white border border-salon-200 peer-checked:border-salon-600 peer-checked:bg-salon-50 text-xs font-bold text-salon-800 transition-all">
                                    <div class="text-lg mb-1">
                                        <c:choose>
                                            <c:when test="${m == 'CASH'}"><i class="fa-solid fa-money-bill-wave text-green-600"></i></c:when>
                                            <c:when test="${m == 'CARD'}"><i class="fa-regular fa-credit-card text-blue-600"></i></c:when>
                                            <c:otherwise><i class="fa-solid fa-mobile-screen-button text-purple-600"></i></c:otherwise>
                                        </c:choose>
                                    </div>
                                    ${m}
                                </div>
                            </label>
                        </c:forEach>
                    </div>
                </div>

                <div class="p-4 bg-amber-50 rounded-2xl border border-amber-200 text-xs text-amber-800">
                    <i class="fa-solid fa-circle-exclamation mr-1.5"></i>
                    This is a mock transaction demonstrating educational OOP &amp; web architecture.
                </div>

                <button type="submit"
                        class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-lg shadow-salon-500/25 transition-all">
                    Authorize &amp; Complete Payment
                </button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
