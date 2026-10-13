<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="pageTitle" value="Membership & Rewards" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-16 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        <!-- Header Banner -->
        <div class="text-center max-w-3xl mx-auto mb-16">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500 bg-salon-50 px-4 py-1.5 rounded-full border border-salon-100">
                Exclusive Privileges
            </span>
            <h1 class="text-4xl sm:text-5xl font-serif font-bold text-salon-900 mt-4 leading-tight">
                Glamour Privé Membership &amp; Rewards
            </h1>
            <p class="text-sm sm:text-base text-salon-600 mt-4 font-light leading-relaxed">
                Elevate your wellness journey with bespoke beauty tiers, priority reservation windows, curated gifts, and reward points redeemable across all luxury treatments.
            </p>
        </div>

        <!-- Membership Tiers Grid -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mb-20 items-stretch">
            
            <!-- Tier 1: Silver Glow -->
            <div class="bg-salon-50/60 rounded-3xl p-8 border border-salon-100 hover:shadow-xl transition-all duration-300 flex flex-col justify-between group">
                <div>
                    <div class="flex items-center justify-between mb-4">
                        <span class="w-12 h-12 rounded-2xl bg-white shadow-sm flex items-center justify-center text-slate-500 text-xl group-hover:scale-110 transition-transform">
                            <i class="fa-solid fa-gem"></i>
                        </span>
                        <span class="text-[11px] font-bold uppercase tracking-wider text-slate-600 bg-slate-100 px-3 py-1 rounded-full">
                            Entry Level
                        </span>
                    </div>
                    <h3 class="text-2xl font-serif font-bold text-salon-900">Silver Glow</h3>
                    <p class="text-xs text-salon-500 mt-1 mb-6">Complimentary upon registration</p>

                    <div class="space-y-3 pt-4 border-t border-salon-200/60 text-xs text-salon-700">
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Earn <strong>1 Point</strong> per Rs. 100 spent</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Welcome voucher for <strong>5% OFF</strong></span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Seasonal beauty alerts &amp; newsletters</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Easy appointment management</span>
                        </div>
                    </div>
                </div>

                <div class="mt-8 pt-6 border-t border-salon-100">
                    <sec:authorize access="isAnonymous()">
                        <a href="${pageContext.request.contextPath}/register" class="block w-full text-center text-xs font-bold uppercase tracking-wider text-salon-800 bg-white hover:bg-salon-100 py-3.5 rounded-2xl border border-salon-200 transition-all shadow-sm">
                            Join Free Today
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <div class="text-center text-xs font-semibold text-salon-600 py-2">
                            <i class="fa-solid fa-check mr-1 text-green-600"></i> Active on your account
                        </div>
                    </sec:authorize>
                </div>
            </div>

            <!-- Tier 2: Rose Gold (Featured) -->
            <div class="bg-gradient-to-b from-salon-800 to-salon-900 text-white rounded-3xl p-8 border border-salon-700 shadow-xl flex flex-col justify-between relative transform md:-translate-y-2">
                <div class="absolute -top-3.5 left-1/2 -translate-x-1/2 bg-salon-500 text-white px-4 py-1 rounded-full text-[10px] font-bold uppercase tracking-widest shadow-md">
                    Most Celebrated
                </div>
                <div>
                    <div class="flex items-center justify-between mb-4">
                        <span class="w-12 h-12 rounded-2xl bg-white/10 backdrop-blur-md shadow-sm flex items-center justify-center text-salon-300 text-xl">
                            <i class="fa-solid fa-crown"></i>
                        </span>
                        <span class="text-[11px] font-bold uppercase tracking-wider text-salon-200 bg-white/10 px-3 py-1 rounded-full">
                            5+ Bookings
                        </span>
                    </div>
                    <h3 class="text-2xl font-serif font-bold text-white">Rose Gold Privé</h3>
                    <p class="text-xs text-salon-200 mt-1 mb-6">Unlocked after 5 completed treatments</p>

                    <div class="space-y-3 pt-4 border-t border-white/10 text-xs text-salon-100">
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-300"></i>
                            <span>Earn <strong>1.5 Points</strong> per Rs. 100 spent</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-300"></i>
                            <span><strong>10% OFF</strong> all curated packages</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-300"></i>
                            <span>Complimentary birthday facial upgrade</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-300"></i>
                            <span>Priority booking slots during peak hours</span>
                        </div>
                    </div>
                </div>

                <div class="mt-8 pt-6 border-t border-white/10">
                    <sec:authorize access="isAnonymous()">
                        <a href="${pageContext.request.contextPath}/register" class="block w-full text-center text-xs font-bold uppercase tracking-wider text-salon-900 bg-salon-200 hover:bg-white py-3.5 rounded-2xl transition-all shadow-md">
                            Register to Unlock
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="${pageContext.request.contextPath}/customer/appointments/book" class="block w-full text-center text-xs font-bold uppercase tracking-wider text-salon-900 bg-salon-200 hover:bg-white py-3.5 rounded-2xl transition-all shadow-md">
                            Book to Advance Tier
                        </a>
                    </sec:authorize>
                </div>
            </div>

            <!-- Tier 3: Platinum Radiance -->
            <div class="bg-salon-50/60 rounded-3xl p-8 border border-salon-100 hover:shadow-xl transition-all duration-300 flex flex-col justify-between group">
                <div>
                    <div class="flex items-center justify-between mb-4">
                        <span class="w-12 h-12 rounded-2xl bg-white shadow-sm flex items-center justify-center text-amber-600 text-xl group-hover:scale-110 transition-transform">
                            <i class="fa-solid fa-wand-magic-sparkles"></i>
                        </span>
                        <span class="text-[11px] font-bold uppercase tracking-wider text-amber-700 bg-amber-50 px-3 py-1 rounded-full border border-amber-200">
                            VIP Sanctuary
                        </span>
                    </div>
                    <h3 class="text-2xl font-serif font-bold text-salon-900">Platinum Radiance</h3>
                    <p class="text-xs text-salon-500 mt-1 mb-6">Reserved for our distinguished guests</p>

                    <div class="space-y-3 pt-4 border-t border-salon-200/60 text-xs text-salon-700">
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Earn <strong>2 Points</strong> per Rs. 100 spent</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span><strong>15% OFF</strong> on all services &amp; packages</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Dedicated master stylist selection preference</span>
                        </div>
                        <div class="flex items-center space-x-2.5">
                            <i class="fa-solid fa-circle-check text-salon-500"></i>
                            <span>Complimentary botanical beverage &amp; aroma therapy</span>
                        </div>
                    </div>
                </div>

                <div class="mt-8 pt-6 border-t border-salon-100">
                    <sec:authorize access="isAnonymous()">
                        <a href="${pageContext.request.contextPath}/register" class="block w-full text-center text-xs font-bold uppercase tracking-wider text-salon-800 bg-white hover:bg-salon-100 py-3.5 rounded-2xl border border-salon-200 transition-all shadow-sm">
                            Begin Journey
                        </a>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <a href="${pageContext.request.contextPath}/packages" class="block w-full text-center text-xs font-bold uppercase tracking-wider text-salon-800 bg-white hover:bg-salon-100 py-3.5 rounded-2xl border border-salon-200 transition-all shadow-sm">
                            Explore Luxury Packages
                        </a>
                    </sec:authorize>
                </div>
            </div>
        </div>

        <!-- How Rewards Work Section -->
        <div class="bg-salon-50/70 rounded-3xl p-10 border border-salon-100 mb-16">
            <div class="text-center max-w-xl mx-auto mb-10">
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Simplicity &amp; Grace</span>
                <h2 class="text-3xl font-serif font-bold text-salon-900 mt-2">How It Works</h2>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div class="text-center p-6 bg-white rounded-2xl shadow-sm border border-salon-100">
                    <div class="w-12 h-12 mx-auto rounded-full bg-salon-100 text-salon-700 flex items-center justify-center font-serif font-bold text-lg mb-4">
                        1
                    </div>
                    <h4 class="font-serif font-bold text-base text-salon-900 mb-2">Book &amp; Experience</h4>
                    <p class="text-xs text-salon-600 leading-relaxed font-light">
                        Reserve any haircut, spa session, facial, or bundle package through our online portal.
                    </p>
                </div>

                <div class="text-center p-6 bg-white rounded-2xl shadow-sm border border-salon-100">
                    <div class="w-12 h-12 mx-auto rounded-full bg-salon-100 text-salon-700 flex items-center justify-center font-serif font-bold text-lg mb-4">
                        2
                    </div>
                    <h4 class="font-serif font-bold text-base text-salon-900 mb-2">Collect Wellness Points</h4>
                    <p class="text-xs text-salon-600 leading-relaxed font-light">
                        Points are automatically credited to your customer profile upon invoice settlement.
                    </p>
                </div>

                <div class="text-center p-6 bg-white rounded-2xl shadow-sm border border-salon-100">
                    <div class="w-12 h-12 mx-auto rounded-full bg-salon-100 text-salon-700 flex items-center justify-center font-serif font-bold text-lg mb-4">
                        3
                    </div>
                    <h4 class="font-serif font-bold text-base text-salon-900 mb-2">Redeem Rewards</h4>
                    <p class="text-xs text-salon-600 leading-relaxed font-light">
                        Enjoy instant percentage discounts and celebratory rewards on your recurring visits.
                    </p>
                </div>
            </div>
        </div>

        <!-- Call to Action Banner -->
        <div class="bg-gradient-to-r from-salon-600 to-salon-800 rounded-3xl p-10 sm:p-12 text-white flex flex-col md:flex-row items-center justify-between gap-8 shadow-lg">
            <div class="max-w-xl text-center md:text-left">
                <h3 class="text-2xl sm:text-3xl font-serif font-bold">Ready to Immerse in Indulgence?</h3>
                <p class="text-xs sm:text-sm text-salon-100 mt-2 font-light">
                    Join our wellness collective or book your personalized appointment today.
                </p>
            </div>
            <div class="flex flex-col sm:flex-row gap-3 w-full md:w-auto">
                <a href="${pageContext.request.contextPath}/customer/appointments/book" class="text-center text-xs font-bold uppercase tracking-wider text-salon-900 bg-white hover:bg-salon-50 px-8 py-3.5 rounded-full transition-all shadow-md">
                    Reserve Appointment
                </a>
                <a href="${pageContext.request.contextPath}/packages" class="text-center text-xs font-bold uppercase tracking-wider text-white bg-salon-700 hover:bg-salon-900 px-8 py-3.5 rounded-full border border-white/20 transition-all">
                    View Packages
                </a>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
