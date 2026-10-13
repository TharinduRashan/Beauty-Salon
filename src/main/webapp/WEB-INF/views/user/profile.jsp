<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Client Profile" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <h1 class="text-3xl font-serif font-bold text-salon-900">Account Profile</h1>
            <p class="text-xs text-salon-500 mt-1">Manage personal contact details and password.</p>
        </div>

        <div class="bg-salon-50/70 p-8 rounded-3xl border border-salon-100 space-y-6">
            <form action="${pageContext.request.contextPath}/customer/profile/edit" method="post" class="space-y-4">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div>
                    <label for="fullName" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Full Name</label>
                    <input type="text" id="fullName" name="fullName" value="<c:out value='${user.fullName}'/>" required
                           class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>

                <div>
                    <label for="email" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Email Address</label>
                    <input type="email" id="email" name="email" value="<c:out value='${user.email}'/>" required
                           class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>

                <div>
                    <label for="phone" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Phone</label>
                    <input type="tel" id="phone" name="phone" value="<c:out value='${user.phone}'/>"
                           class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>

                <button type="submit" class="text-xs font-semibold text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all">
                    Update Profile
                </button>
            </form>

            <hr class="border-salon-200/60">

            <form action="${pageContext.request.contextPath}/customer/profile/change-password" method="post" class="space-y-4">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <h3 class="font-serif text-lg font-bold text-salon-900">Change Password</h3>

                <div>
                    <label for="currentPassword" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Current Password</label>
                    <input type="password" id="currentPassword" name="currentPassword" required
                           class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="newPassword" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">New Password</label>
                        <input type="password" id="newPassword" name="newPassword" required minlength="8"
                               class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                    </div>
                    <div>
                        <label for="confirmPassword" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8"
                               class="w-full px-4 py-2.5 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                    </div>
                </div>

                <button type="submit" class="text-xs font-semibold text-white bg-salon-800 hover:bg-black px-6 py-3 rounded-full transition-all">
                    Change Password
                </button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
