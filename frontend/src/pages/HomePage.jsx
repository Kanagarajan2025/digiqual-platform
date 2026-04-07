import { Link } from 'react-router-dom'
import SiteHeader from '../components/SiteHeader'

export default function HomePage() {
  return (
    <div className="min-h-screen">
      <SiteHeader />
      <main className="mx-auto max-w-6xl px-4 py-10 md:px-6">
        <section className="grid gap-8 rounded-3xl border border-slate-200 bg-white p-8 shadow-[0_20px_70px_rgba(15,23,42,0.1)] md:grid-cols-2 md:items-center">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.18em] text-orange-600">UK Awarding Body</p>
            <h1 className="mt-3 font-display text-4xl font-semibold text-slate-950">Accreditation standards for short professional courses.</h1>
            <p className="mt-4 text-sm leading-7 text-slate-600">
              DIGIQUAL provides syllabus, quality assurance, and certificate accreditation through approved partner institutes.
              We are not a college; we certify delivery through partners.
            </p>
            <div className="mt-6 flex flex-wrap gap-3">
              <Link to="/courses" className="rounded-xl bg-orange-600 px-4 py-2 text-sm font-semibold text-white hover:bg-orange-700">Explore Courses</Link>
              <Link to="/certificate-verification" className="rounded-xl border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:border-orange-400 hover:text-orange-600">Verify Certificate</Link>
            </div>
          </div>
          <div className="grid gap-3 text-sm">
            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4"><strong>Partner Governance:</strong> Approval, suspension, and capacity controls.</div>
            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4"><strong>Student Lifecycle:</strong> Enrolment review, activation, completion, certification.</div>
            <div className="rounded-2xl border border-slate-200 bg-slate-50 p-4"><strong>Public Trust:</strong> Certificate verification by Student ID and surname.</div>
          </div>
        </section>
      </main>
    </div>
  )
}
