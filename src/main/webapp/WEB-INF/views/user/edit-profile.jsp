<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit Profile" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <a href="${pageContext.request.contextPath}/customer/profile" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">
                &larr; Back to Profile
            </a>
            <h1 class="text-3xl font-serif font-bold text-salon-900">Edit Profile</h1>
        </div>

        <form action="${pageContext.request.contextPath}/customer/profile/edit" method="post" class="space-y-6 bg-salon-50/70 p-8 rounded-3xl border border-salon-100 shadow-sm">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="fullName" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${user.fullName}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
            </div>

            <div>
                <label for="email" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Email Address</label>
                <input type="email" id="email" name="email" value="<c:out value='${user.email}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
            </div>

            <div>
                <label for="phone" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Phone</label>
                <input type="tel" id="phone" name="phone" value="<c:out value='${user.phone}'/>"
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
            </div>

            <button type="submit" class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-sm transition-all">
                Save Changes
            </button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
