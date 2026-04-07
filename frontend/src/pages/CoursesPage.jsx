import { Link } from 'react-router-dom'
import SiteHeader from '../components/SiteHeader'
import { courses } from '../data/courses'

export default function CoursesPage() {
  return (
    <div className="min-h-screen">
      <SiteHeader />
      <main className="mx-auto max-w-6xl px-4 py-10 md:px-6">
        <section className="mb-6">
          <h1 className="font-display text-3xl font-semibold text-slate-950">Courses</h1>
          <p className="mt-2 text-sm text-slate-600">Main course listing with 13 accredited short-course pathways.</p>
        </section>

        <section className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
          {courses.map((course) => (
            <article key={course.slug} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-[0_12px_40px_rgba(15,23,42,0.08)]">
              <h2 className="font-display text-lg font-semibold text-slate-900">{course.title}</h2>
              <p className="mt-2 text-sm text-slate-600">{course.summary}</p>
              <div className="mt-3 text-xs text-slate-500">{course.duration} · {course.mode}</div>
              <Link to={`/courses/${course.slug}`} className="mt-4 inline-block text-sm font-semibold text-orange-600 hover:text-orange-700">
                View course page
              </Link>
            </article>
          ))}
        </section>
      </main>
    </div>
  )
}
