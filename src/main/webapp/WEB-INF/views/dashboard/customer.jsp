<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Customer Dashboard" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-10 pb-6 border-b border-salon-100 flex flex-col sm:flex-row justify-between items-start sm:items-center">
            <div>
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Welcome Back</span>
                <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1"><c:out value="${user.fullName}"/></h1>
                <p class="text-xs text-salon-500 mt-1">Manage your wellness calendar, reviews, and receipts effortlessly.</p>
            </div>
            <a href="${pageContext.request.contextPath}/customer/appointments/book"
               class="mt-4 sm:mt-0 text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all shadow-sm">
                Book Treatment
            </a>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <a href="${pageContext.request.contextPath}/customer/appointments"
               class="p-6 rounded-3xl bg-salon-50/70 border border-salon-100 hover:shadow-lg hover:bg-white transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white flex items-center justify-center text-salon-600 shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-regular fa-calendar text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Appointments</h3>
                <p class="text-xs text-salon-600 mt-1 font-light">View your upcoming reservations, dates, and manage appointments.</p>
            </a>

            <a href="${pageContext.request.contextPath}/customer/payments"
               class="p-6 rounded-3xl bg-salon-50/70 border border-salon-100 hover:shadow-lg hover:bg-white transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white flex items-center justify-center text-salon-600 shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-receipt text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Payments &amp; Receipts</h3>
                <p class="text-xs text-salon-600 mt-1 font-light">Access your invoice histories and printable payment receipts.</p>
            </a>

            <a href="${pageContext.request.contextPath}/customer/profile"
               class="p-6 rounded-3xl bg-salon-50/70 border border-salon-100 hover:shadow-lg hover:bg-white transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white flex items-center justify-center text-salon-600 shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-regular fa-id-badge text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Profile Details</h3>
                <p class="text-xs text-salon-600 mt-1 font-light">Update contact information, phone numbers, or account security.</p>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
