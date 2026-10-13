<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Staff Dashboard" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-10 pb-6 border-b border-salon-100">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Staff Portal</span>
            <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1">Hello, <c:out value="${user.fullName}"/></h1>
            <p class="text-xs text-salon-500 mt-1">Review your weekly appointments schedule, assigned working hours, and clients.</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <a href="${pageContext.request.contextPath}/staff/schedule" class="p-8 rounded-3xl bg-salon-50/70 border border-salon-100 hover:shadow-lg hover:bg-white transition-all group">
                <div class="w-12 h-12 rounded-2xl bg-white text-salon-600 flex items-center justify-center shadow-sm mb-4 group-hover:scale-105 transition-transform">
                    <i class="fa-solid fa-calendar-week text-xl"></i>
                </div>
                <h3 class="font-serif text-lg font-bold text-salon-900">My Working Roster</h3>
                <p class="text-xs text-salon-600 mt-1">Inspect working shifts, daily break intervals, and approved time-off.</p>
            </a>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
