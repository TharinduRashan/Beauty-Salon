<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Sign In" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="flex-grow flex items-center justify-center py-16 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full bg-white rounded-3xl shadow-xl border border-salon-100 p-8 sm:p-10">
        <div class="text-center mb-8">
            <span class="w-12 h-12 rounded-2xl bg-salon-50 text-salon-600 flex items-center justify-center mx-auto mb-4 text-xl">
                <i class="fa-solid fa-lock"></i>
            </span>
            <h2 class="text-2xl font-serif font-bold text-salon-900">Welcome Back</h2>
            <p class="text-xs text-salon-500 mt-2">Sign in to manage your bookings and experience personalized care</p>
        </div>

        <form action="${pageContext.request.contextPath}/login" method="post" class="space-y-6">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="username" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Username</label>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-salon-400">
                        <i class="fa-regular fa-user text-sm"></i>
                    </span>
                    <input type="text" id="username" name="username" required
                           class="w-full pl-10 pr-4 py-3 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white transition-all text-salon-900"
                           placeholder="Enter your username">
                </div>
            </div>

            <div>
                <label for="password" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Password</label>
                <div class="relative">
                    <span class="absolute inset-y-0 left-0 pl-3.5 flex items-center text-salon-400">
                        <i class="fa-solid fa-key text-sm"></i>
                    </span>
                    <input type="password" id="password" name="password" required
                           class="w-full pl-10 pr-4 py-3 bg-salon-50/50 border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 focus:bg-white transition-all text-salon-900"
                           placeholder="••••••••">
                </div>
            </div>

            <button type="submit"
                    class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-lg shadow-salon-500/25 transition-all duration-200">
                Sign In
            </button>
        </form>

        <div class="mt-8 pt-6 border-t border-salon-100 text-center">
            <p class="text-xs text-salon-600">
                Don't have an account yet?
                <a href="${pageContext.request.contextPath}/register" class="font-bold text-salon-700 hover:text-salon-900 ml-1">Create an account</a>
            </p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
