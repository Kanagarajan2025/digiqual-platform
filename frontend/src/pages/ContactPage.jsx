import SiteHeader from '../components/SiteHeader'

export default function ContactPage() {
  return (
    <div className="min-h-screen">
      <SiteHeader />
      <main className="mx-auto max-w-4xl px-4 py-10 md:px-6">
        <section className="rounded-3xl border border-slate-200 bg-white p-8 shadow-[0_20px_70px_rgba(15,23,42,0.1)]">
          <h1 className="font-display text-3xl font-semibold text-slate-950">Contact DIGIQUAL</h1>
          <p className="mt-3 text-sm leading-7 text-slate-600">
            For partner onboarding, accreditation queries, and certificate support, contact the DIGIQUAL operations team.
          </p>
          <div className="mt-6 grid gap-3 rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700">
            <p><strong>Email:</strong> support@digiqual.co.uk</p>
            <p><strong>Partner Enquiries:</strong> partners@digiqual.co.uk</p>
            <p><strong>Hours:</strong> Mon-Fri, 09:00-17:00 UK time</p>
          </div>
        </section>
      </main>
    </div>
  )
}
