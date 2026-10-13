<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="bg-white/90 backdrop-blur-md sticky top-0 z-50 border-b border-salon-100 shadow-sm transition-all duration-300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-20">
            <!-- Brand -->
            <div class="flex items-center space-x-3">
                <a href="${pageContext.request.contextPath}/" class="flex items-center space-x-2">
                    <span class="w-10 h-10 rounded-full bg-gradient-to-tr from-salon-600 to-salon-400 flex items-center justify-center text-white shadow-md">
                        <i class="fa-solid fa-spa text-lg"></i>
                    </span>
                    <span class="text-2xl font-serif font-bold tracking-wide text-salon-900">Glamour <span class="text-salon-500 font-light italic">Salon</span></span>
                </a>
            </div>

            <!-- Desktop Nav Links -->
            <div class="hidden md:flex items-center space-x-8 text-sm font-medium">
                <a href="${pageContext.request.contextPath}/" class="text-salon-800 hover:text-salon-500 transition-colors">Home</a>
                <a href="${pageContext.request.contextPath}/services" class="text-salon-800 hover:text-salon-500 transition-colors">Services</a>
                <a href="${pageContext.request.contextPath}/packages" class="text-salon-800 hover:text-salon-500 transition-colors">Packages</a>
                <a href="${pageContext.request.contextPath}/reviews" class="text-salon-800 hover:text-salon-500 transition-colors">Reviews</a>
            </div>

            <!-- Desktop Actions / Auth Profile -->
            <div class="hidden md:flex items-center space-x-4">
                <sec:authorize access="isAnonymous()">
                    <a href="${pageContext.request.contextPath}/login" class="text-sm font-semibold text-salon-700 hover:text-salon-900 px-4 py-2">Sign In</a>
                    <a href="${pageContext.request.contextPath}/register" class="text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 px-5 py-2.5 rounded-full shadow-sm hover:shadow transition-all">Book Appointment</a>
                </sec:authorize>

                <sec:authorize access="hasRole('CUSTOMER')">
                    <a href="${pageContext.request.contextPath}/customer/appointments/book" class="text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 px-5 py-2.5 rounded-full shadow-sm hover:shadow transition-all">
                        <i class="fa-regular fa-calendar-plus mr-1.5"></i> Book Now
                    </a>
                    <div class="relative group" id="user-dropdown-container">
                        <button type="button" id="user-dropdown-btn"
                                aria-expanded="false" aria-haspopup="true"
                                class="flex items-center space-x-2 text-sm font-medium text-salon-800 bg-salon-50 hover:bg-salon-100 px-4 py-2 rounded-full border border-salon-200 transition-all cursor-pointer focus:outline-none">
                            <i class="fa-regular fa-circle-user text-salon-600 text-base"></i>
                            <span><sec:authentication property="name"/></span>
                            <i class="fa-solid fa-chevron-down text-xs text-salon-400 transition-transform duration-200 group-hover:rotate-180" id="user-dropdown-arrow"></i>
                        </button>
                        <!-- Seamless Bridge Wrapper & Dropdown Menu -->
                        <div id="user-dropdown-menu"
                             class="absolute right-0 top-full pt-1.5 w-52 hidden group-hover:block transition-all z-50">
                            <div class="bg-white rounded-2xl shadow-xl border border-salon-100 py-2 ring-1 ring-black/5">
                                <div class="px-4 py-2 border-b border-salon-100 mb-1">
                                    <p class="text-[10px] uppercase tracking-wider font-semibold text-salon-400">Signed in as</p>
                                    <p class="text-xs font-bold text-salon-900 truncate"><sec:authentication property="name"/></p>
                                </div>
                                <a href="${pageContext.request.contextPath}/customer/dashboard" class="flex items-center px-4 py-2 text-xs font-semibold text-salon-700 hover:bg-salon-50 hover:text-salon-900 transition-colors">
                                    <i class="fa-solid fa-gauge w-5 text-salon-400"></i>Dashboard
                                </a>
                                <a href="${pageContext.request.contextPath}/customer/appointments" class="flex items-center px-4 py-2 text-xs font-semibold text-salon-700 hover:bg-salon-50 hover:text-salon-900 transition-colors">
                                    <i class="fa-regular fa-calendar w-5 text-salon-400"></i>My Appointments
                                </a>
                                <a href="${pageContext.request.contextPath}/customer/payments" class="flex items-center px-4 py-2 text-xs font-semibold text-salon-700 hover:bg-salon-50 hover:text-salon-900 transition-colors">
                                    <i class="fa-solid fa-receipt w-5 text-salon-400"></i>Billing &amp; Receipts
                                </a>
                                <a href="${pageContext.request.contextPath}/customer/feedback" class="flex items-center px-4 py-2 text-xs font-semibold text-salon-700 hover:bg-salon-50 hover:text-salon-900 transition-colors">
                                    <i class="fa-regular fa-star w-5 text-salon-400"></i>My Reviews
                                </a>
                                <a href="${pageContext.request.contextPath}/customer/profile" class="flex items-center px-4 py-2 text-xs font-semibold text-salon-700 hover:bg-salon-50 hover:text-salon-900 transition-colors">
                                    <i class="fa-regular fa-id-badge w-5 text-salon-400"></i>Profile Settings
                                </a>
                                <hr class="my-1 border-salon-100">
                                <form action="${pageContext.request.contextPath}/logout" method="post" class="m-0">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="w-full flex items-center px-4 py-2 text-xs font-semibold text-red-600 hover:bg-red-50 transition-colors">
                                        <i class="fa-solid fa-arrow-right-from-bracket w-5 text-red-400"></i>Sign Out
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </sec:authorize>

                <sec:authorize access="hasRole('STAFF')">
                    <a href="${pageContext.request.contextPath}/staff/dashboard" class="text-sm font-semibold text-white bg-salon-700 hover:bg-salon-800 px-4 py-2 rounded-full">
                        <i class="fa-solid fa-calendar-days mr-1.5"></i> Staff Portal
                    </a>
                    <form action="${pageContext.request.contextPath}/logout" method="post" class="inline m-0">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="text-sm text-salon-600 hover:text-salon-900 font-medium px-2 py-1">Logout</button>
                    </form>
                </sec:authorize>

                <sec:authorize access="hasRole('ADMIN')">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-sm font-semibold text-white bg-slate-900 hover:bg-black px-4 py-2 rounded-full shadow-sm">
                        <i class="fa-solid fa-shield-halved mr-1.5"></i> Admin Console
                    </a>
                    <form action="${pageContext.request.contextPath}/logout" method="post" class="inline m-0">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="text-sm text-salon-600 hover:text-salon-900 font-medium px-2 py-1">Logout</button>
                    </form>
                </sec:authorize>
            </div>

            <!-- Mobile Hamburger Toggle Button -->
            <div class="flex md:hidden items-center">
                <button type="button" id="mobile-menu-btn" aria-label="Toggle Navigation Menu"
                        class="p-2.5 rounded-2xl bg-salon-50 text-salon-800 hover:bg-salon-100 focus:outline-none transition-colors border border-salon-200/60">
                    <i class="fa-solid fa-bars text-xl" id="menu-icon"></i>
                </button>
            </div>
        </div>
    </div>

    <!-- Mobile Dropdown Navigation Drawer -->
    <div id="mobile-menu" class="hidden md:hidden border-t border-salon-100 bg-white/95 backdrop-blur-md px-4 pt-3 pb-6 space-y-3 shadow-lg animate-fade-in">
        <div class="space-y-1">
            <a href="${pageContext.request.contextPath}/" class="block px-3 py-2.5 rounded-xl text-sm font-medium text-salon-800 hover:bg-salon-50 hover:text-salon-600">
                <i class="fa-solid fa-house w-6 text-salon-400"></i> Home
            </a>
            <a href="${pageContext.request.contextPath}/services" class="block px-3 py-2.5 rounded-xl text-sm font-medium text-salon-800 hover:bg-salon-50 hover:text-salon-600">
                <i class="fa-solid fa-scissors w-6 text-salon-400"></i> Services
            </a>
            <a href="${pageContext.request.contextPath}/packages" class="block px-3 py-2.5 rounded-xl text-sm font-medium text-salon-800 hover:bg-salon-50 hover:text-salon-600">
                <i class="fa-solid fa-box-open w-6 text-salon-400"></i> Packages
            </a>
            <a href="${pageContext.request.contextPath}/reviews" class="block px-3 py-2.5 rounded-xl text-sm font-medium text-salon-800 hover:bg-salon-50 hover:text-salon-600">
                <i class="fa-regular fa-comments w-6 text-salon-400"></i> Reviews
            </a>
            <a href="${pageContext.request.contextPath}/membership" class="block px-3 py-2.5 rounded-xl text-sm font-medium text-salon-800 hover:bg-salon-50 hover:text-salon-600">
                <i class="fa-solid fa-crown w-6 text-salon-400"></i> Membership &amp; Rewards
            </a>
        </div>

        <hr class="border-salon-100 my-2">

        <!-- Mobile User Options -->
        <sec:authorize access="isAnonymous()">
            <div class="pt-1 flex flex-col gap-2">
                <a href="${pageContext.request.contextPath}/login" class="w-full text-center py-2.5 text-sm font-semibold text-salon-700 bg-salon-50 rounded-xl hover:bg-salon-100 border border-salon-200">
                    Sign In
                </a>
                <a href="${pageContext.request.contextPath}/register" class="w-full text-center py-2.5 text-sm font-semibold text-white bg-salon-600 rounded-xl hover:bg-salon-700 shadow-sm">
                    Book Appointment
                </a>
            </div>
        </sec:authorize>

        <sec:authorize access="hasRole('CUSTOMER')">
            <div class="space-y-1">
                <div class="px-3 py-1.5 text-xs uppercase tracking-wider font-bold text-salon-400">
                    Signed in as <span class="text-salon-900 font-semibold"><sec:authentication property="name"/></span>
                </div>
                <a href="${pageContext.request.contextPath}/customer/appointments/book" class="block px-3 py-2 rounded-xl text-sm font-semibold text-white bg-salon-600 text-center mb-2 shadow-sm">
                    <i class="fa-regular fa-calendar-plus mr-1.5"></i> Book Appointment
                </a>
                <a href="${pageContext.request.contextPath}/customer/dashboard" class="block px-3 py-2 rounded-xl text-sm font-medium text-salon-700 hover:bg-salon-50">
                    <i class="fa-solid fa-gauge w-6 text-salon-400"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/customer/appointments" class="block px-3 py-2 rounded-xl text-sm font-medium text-salon-700 hover:bg-salon-50">
                    <i class="fa-regular fa-calendar w-6 text-salon-400"></i> My Appointments
                </a>
                <a href="${pageContext.request.contextPath}/customer/payments" class="block px-3 py-2 rounded-xl text-sm font-medium text-salon-700 hover:bg-salon-50">
                    <i class="fa-solid fa-receipt w-6 text-salon-400"></i> Billing & Receipts
                </a>
                <a href="${pageContext.request.contextPath}/customer/feedback" class="block px-3 py-2 rounded-xl text-sm font-medium text-salon-700 hover:bg-salon-50">
                    <i class="fa-regular fa-star w-6 text-salon-400"></i> My Reviews
                </a>
                <a href="${pageContext.request.contextPath}/customer/profile" class="block px-3 py-2 rounded-xl text-sm font-medium text-salon-700 hover:bg-salon-50">
                    <i class="fa-regular fa-id-badge w-6 text-salon-400"></i> Profile Settings
                </a>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="pt-2 m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="w-full text-left px-3 py-2 rounded-xl text-sm font-semibold text-red-600 hover:bg-red-50">
                        <i class="fa-solid fa-arrow-right-from-bracket w-6 text-red-400"></i> Sign Out
                    </button>
                </form>
            </div>
        </sec:authorize>

        <sec:authorize access="hasRole('STAFF')">
            <div class="space-y-1">
                <a href="${pageContext.request.contextPath}/staff/dashboard" class="block px-3 py-2 rounded-xl text-sm font-semibold text-white bg-salon-700 text-center">
                    <i class="fa-solid fa-calendar-days mr-1.5"></i> Staff Portal
                </a>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="pt-2 m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="w-full text-left px-3 py-2 rounded-xl text-sm font-semibold text-red-600 hover:bg-red-50">
                        <i class="fa-solid fa-arrow-right-from-bracket w-6 text-red-400"></i> Logout
                    </button>
                </form>
            </div>
        </sec:authorize>

        <sec:authorize access="hasRole('ADMIN')">
            <div class="space-y-1">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="block px-3 py-2 rounded-xl text-sm font-semibold text-white bg-slate-900 text-center">
                    <i class="fa-solid fa-shield-halved mr-1.5"></i> Admin Console
                </a>
                <form action="${pageContext.request.contextPath}/logout" method="post" class="pt-2 m-0">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="w-full text-left px-3 py-2 rounded-xl text-sm font-semibold text-red-600 hover:bg-red-50">
                        <i class="fa-solid fa-arrow-right-from-bracket w-6 text-red-400"></i> Logout
                    </button>
                </form>
            </div>
        </sec:authorize>
    </div>
</nav>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Mobile Navigation Drawer Toggle
        var menuBtn = document.getElementById('mobile-menu-btn');
        var mobileMenu = document.getElementById('mobile-menu');
        var menuIcon = document.getElementById('menu-icon');

        if (menuBtn && mobileMenu) {
            menuBtn.addEventListener('click', function () {
                var isHidden = mobileMenu.classList.contains('hidden');
                if (isHidden) {
                    mobileMenu.classList.remove('hidden');
                    menuIcon.classList.remove('fa-bars');
                    menuIcon.classList.add('fa-xmark');
                } else {
                    mobileMenu.classList.add('hidden');
                    menuIcon.classList.remove('fa-xmark');
                    menuIcon.classList.add('fa-bars');
                }
            });
        }

        // Customer Profile Dropdown Click & Hover Management
        var userBtn = document.getElementById('user-dropdown-btn');
        var userMenu = document.getElementById('user-dropdown-menu');
        var userArrow = document.getElementById('user-dropdown-arrow');

        if (userBtn && userMenu) {
            userBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                var isOpen = !userMenu.classList.contains('hidden');
                if (isOpen) {
                    userMenu.classList.add('hidden');
                    if (userArrow) userArrow.classList.remove('rotate-180');
                } else {
                    userMenu.classList.remove('hidden');
                    if (userArrow) userArrow.classList.add('rotate-180');
                }
            });

            // Close on click outside
            document.addEventListener('click', function (e) {
                if (!userMenu.contains(e.target) && !userBtn.contains(e.target)) {
                    userMenu.classList.add('hidden');
                    if (userArrow) userArrow.classList.remove('rotate-180');
                }
            });

            // Close on Escape key
            document.addEventListener('keydown', function (e) {
                if (e.key === 'Escape') {
                    userMenu.classList.add('hidden');
                    if (userArrow) userArrow.classList.remove('rotate-180');
                }
            });
        }
    });
</script>
