import SiteHeader from '../components/SiteHeader'

export default function AboutPage() {
  return (
    <div className="min-h-screen">
      <SiteHeader />
      <main className="mx-auto max-w-5xl px-4 py-10 md:px-6">
        <section className="rounded-3xl border border-slate-200 bg-white p-8 shadow-[0_20px_70px_rgba(15,23,42,0.1)]">
          <h1 className="font-display text-3xl font-semibold text-slate-950">About DIGIQUAL</h1>
          <p className="mt-4 text-sm leading-7 text-slate-600">
            DIGIQUAL is a UK-based awarding body for short courses. We provide curriculum standards,
            quality oversight, and accreditation through approved partner institutes.
          </p>
          <p className="mt-4 text-sm leading-7 text-slate-600">
            Our central platform supports partner operations, student lifecycle management,
            certificate issuance controls, and public verification.
          </p>
        </section>
      </main>
    </div>
  )
}
