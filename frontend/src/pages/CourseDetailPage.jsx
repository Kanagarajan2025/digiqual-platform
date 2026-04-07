import { Link, useParams } from 'react-router-dom'
import SiteHeader from '../components/SiteHeader'
import { courses } from '../data/courses'

export default function CourseDetailPage() {
  const { slug } = useParams()
  const course = courses.find((item) => item.slug === slug)

  if (!course) {
    return (
      <div className="min-h-screen">
        <SiteHeader />
        <main className="mx-auto max-w-4xl px-4 py-10 md:px-6">
          <div className="rounded-2xl border border-rose-200 bg-rose-50 p-6 text-rose-700">
            Course page not found. <Link to="/courses" className="font-semibold underline">Back to courses</Link>
          </div>
        </main>
      </div>
    )
  }

  return (
    <div className="min-h-screen">
      <SiteHeader />
      <main className="mx-auto max-w-4xl px-4 py-10 md:px-6">
        <article className="rounded-3xl border border-slate-200 bg-white p-8 shadow-[0_20px_70px_rgba(15,23,42,0.1)]">
          <p className="text-xs font-semibold uppercase tracking-[0.16em] text-orange-600">Individual Course Page</p>
          <h1 className="mt-2 font-display text-3xl font-semibold text-slate-950">{course.title}</h1>
          <p className="mt-4 text-sm leading-7 text-slate-600">{course.summary}</p>

          <div className="mt-6 grid gap-3 rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700 md:grid-cols-2">
            <p><strong>Duration:</strong> {course.duration}</p>
            <p><strong>Delivery:</strong> {course.mode}</p>
            <p><strong>Awarding Body:</strong> DIGIQUAL</p>
            <p><strong>Progression:</strong> Partner-led enrolment and admin approval</p>
          </div>

          <div className="mt-6 flex flex-wrap gap-3">
            <Link to="/partner-login" className="rounded-xl bg-orange-600 px-4 py-2 text-sm font-semibold text-white hover:bg-orange-700">Partner Login</Link>
            <Link to="/courses" className="rounded-xl border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 hover:border-orange-400 hover:text-orange-700">Back to courses</Link>
          </div>
        </article>
      </main>
    </div>
  )
}
