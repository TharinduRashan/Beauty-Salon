<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Payment Receipt" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="bg-salon-50/70 p-8 sm:p-10 rounded-3xl border border-salon-100 shadow-sm print:border-none print:shadow-none print:p-0">
            <!-- Receipt Header -->
            <div class="text-center pb-6 border-b border-salon-200/60">
                <span class="w-12 h-12 rounded-full bg-green-100 text-green-700 flex items-center justify-center mx-auto mb-3 text-xl">
                    <i class="fa-solid fa-check"></i>
                </span>
                <h2 class="text-2xl font-serif font-bold text-salon-900">Glamour Beauty Salon</h2>
                <p class="text-xs text-salon-500 mt-1">Official Payment Receipt &amp; Proof of Service</p>
                <div class="mt-4 inline-block px-3 py-1 bg-green-50 text-green-800 rounded-full text-xs font-semibold">
                    PAID (SIMULATED)
                </div>
            </div>

            <!-- Receipt Meta -->
            <div class="py-6 border-b border-salon-200/60 space-y-2 text-xs text-salon-600">
                <div class="flex justify-between">
                    <span>Receipt No:</span>
                    <span class="font-mono font-semibold text-salon-900">${payment.id}</span>
                </div>
                <div class="flex justify-between">
                    <span>Ref Code:</span>
                    <span class="font-mono font-semibold text-salon-900">${payment.transactionRef}</span>
                </div>
                <div class="flex justify-between">
                    <span>Date &amp; Time:</span>
                    <span class="text-salon-900">${payment.formattedCreatedAt}</span>
                </div>
                <div class="flex justify-between">
                    <span>Client Name:</span>
                    <span class="text-salon-900 font-semibold"><c:out value="${payment.customerName}"/></span>
                </div>
                <div class="flex justify-between">
                    <span>Payment Method:</span>
                    <span class="text-salon-900 font-semibold">${payment.paymentMethod}</span>
                </div>
            </div>

            <!-- Receipt Items -->
            <div class="py-6 border-b border-salon-200/60 space-y-2 text-xs">
                <div class="flex justify-between text-salon-800">
                    <span>Treatment: <strong><c:out value="${payment.serviceNameSnapshot}"/></strong></span>
                    <span>Rs. ${payment.amount}</span>
                </div>
                <div class="flex justify-between text-salon-500">
                    <span>Tax (10% VAT):</span>
                    <span>Rs. ${payment.tax}</span>
                </div>
                <div class="pt-4 flex justify-between text-base font-bold text-salon-900">
                    <span>Total Paid:</span>
                    <span class="text-salon-600">Rs. ${payment.total}</span>
                </div>
            </div>

            <!-- Receipt Actions -->
            <div class="pt-6 flex justify-between items-center print:hidden">
                <a href="${pageContext.request.contextPath}/customer/appointments" class="text-xs font-semibold text-salon-600 hover:text-salon-800">
                    &larr; Back to Appointments
                </a>
                <button type="button" onclick="window.print()" class="text-xs font-bold uppercase tracking-wider text-white bg-salon-800 hover:bg-black px-5 py-2.5 rounded-full">
                    <i class="fa-solid fa-print mr-1.5"></i> Print Receipt
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
