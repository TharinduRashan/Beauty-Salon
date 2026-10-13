<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Create Account" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="flex-grow flex items-center justify-center py-16 px-4 sm:px-6 lg:px-8">
    <div class="max-w-lg w-full bg-white rounded-3xl shadow-xl border border-salon-100 p-8 sm:p-10">
        <div class="text-center mb-8">
            <span class="w-12 h-12 rounded-2xl bg-salon-50 text-salon-600 flex items-center justify-center mx-auto mb-4 text-xl">
                <i class="fa-solid fa-user-plus"></i>
            </span>
            <h2 class="text-2xl font-serif font-bold text-salon-900">Join Glamour Salon</h2>
            <p class="text-xs text-salon-500 mt-2">Unlock effortless online bookings and personalized beauty care</p>
        </div>

        <form action="${pageContext.request.contextPath}/register" method="post" class="space-y-4">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="fullName" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${fullName}'/>" required
                       class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                       placeholder="e.g. Jane Doe">
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label for="username" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Username</label>
                    <input type="text" id="username" name="username" value="<c:out value='${username}'/>" required
                           class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                           placeholder="janedoe">
                </div>
                <div>
                    <label for="phone" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Phone Number</label>
                    <input type="tel" id="phone" name="phone" value="<c:out value='${phone}'/>"
                           class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                           placeholder="+94 77 123 4567">
                </div>
            </div>

            <div>
                <label for="email" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Email Address</label>
                <input type="email" id="email" name="email" value="<c:out value='${email}'/>" required
                       class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                       placeholder="jane@example.com">
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label for="password" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Password</label>
                    <input type="password" id="password" name="password" required minlength="8"
                           class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                           placeholder="Min 8 characters">
                </div>
                <div>
                    <label for="confirmPassword" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-1">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required minlength="8"
                           class="w-full px-4 py-2.5 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white"
                           placeholder="Re-enter password">
                </div>
            </div>

            <div class="pt-2">
                <button type="submit"
                        class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-lg shadow-salon-500/25 transition-all duration-200">
                    Complete Registration
                </button>
            </div>
        </form>

        <div class="mt-8 pt-6 border-t border-salon-100 text-center">
            <p class="text-xs text-salon-600">
                Already registered?
                <a href="${pageContext.request.contextPath}/login" class="font-bold text-salon-700 hover:text-salon-900 ml-1">Sign in here</a>
            </p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
