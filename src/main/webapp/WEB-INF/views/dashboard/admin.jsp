<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin Console" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-10 pb-6 border-b border-salon-100">
            <span class="text-xs uppercase tracking-widest font-semibold text-slate-500">System Administration</span>
            <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1">Management Hub</h1>
            <p class="text-xs text-salon-500 mt-1">Full control over users, catalogue, schedules, transactions, and moderation.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <a href="${pageContext.request.contextPath}/admin/users" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-users text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">User Management</h3>
                <p class="text-xs text-salon-600 mt-1">View users, adjust access permissions, activate/suspend accounts.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/services" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-scissors text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Service Catalogue</h3>
                <p class="text-xs text-salon-600 mt-1">Add, modify, or archive individual salon treatments and pricing.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/packages" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-box-open text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Package Bundles</h3>
                <p class="text-xs text-salon-600 mt-1">Configure multi-treatment promotional packages with discounts.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/staff" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-user-tie text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Staff &amp; Rosters</h3>
                <p class="text-xs text-salon-600 mt-1">Manage staff specialties, weekly shifts, break intervals, and leaves.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/appointments" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-regular fa-calendar-check text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Appointments Control</h3>
                <p class="text-xs text-salon-600 mt-1">Monitor bookings, mark completion, handle cancellations and conflicts.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/payments" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-chart-line text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Payment Auditing</h3>
                <p class="text-xs text-salon-600 mt-1">Audit transaction logs, issue refunds, and inspect financial metrics.</p>
            </a>

            <a href="${pageContext.request.contextPath}/admin/feedback" class="p-6 rounded-3xl bg-slate-50/60 border border-slate-200/80 hover:bg-white hover:shadow-lg transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-slate-800 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-regular fa-thumbs-up text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">Feedback Moderation</h3>
                <p class="text-xs text-salon-600 mt-1">Review customer testimonials, approve or reject public display.</p>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
