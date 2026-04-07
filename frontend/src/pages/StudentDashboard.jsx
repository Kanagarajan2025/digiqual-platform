import { useEffect, useMemo, useState } from 'react'
import { getStudentDashboard } from '../api/studentApi'
import AppShell from '../components/AppShell'
import heroStudent from '../assets/hero-student.svg'

export default function StudentDashboard() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true

    const loadDashboard = async () => {
      try {
        const response = await getStudentDashboard()
        if (active) {
          setData(response)
          setError('')
        }
      } catch (err) {
        if (active) {
          setError(err.response?.data?.message || 'Unable to load student profile right now.')
        }
      } finally {
        if (active) {
          setLoading(false)
        }
      }
    }

    loadDashboard()
    return () => {
      active = false
    }
  }, [])

  const profile = data?.profile || null
  const stats = data?.stats || []
  const quickActions = data?.quickActions || []
  const highlights = data?.highlights || []
  const learningModules = data?.learningModules || []
  const timeline = data?.timeline || []

  const subtitle = profile
    ? `${profile.fullName} (${profile.email}) - ${profile.courseName}`
    : 'Review your approved profile, access study materials, monitor status, and download your certificate once DIGIQUAL marks you complete.'

  return (
    <AppShell
      role="STUDENT"
      title="Student Learning Portal"
      subtitle={subtitle}
      accentClass="from-[#0284c7] via-[#0891b2] to-[#14b8a6]"
      heroImage={heroStudent}
      heroBadge="Learning Progress"
      stats={stats}
      quickLinks={quickActions}
      highlights={highlights}
    >
      {error ? (
        <div className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</div>
      ) : null}

      <div className="grid gap-6 xl:grid-cols-[1.08fr_0.92fr]">
        <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
          <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Profile snapshot</h2>
          <div className="mt-5 grid gap-3 sm:grid-cols-2">
            <div className="rounded-xl border border-slate-200 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">Full name</p>
              <p className="mt-2 text-sm font-semibold text-slate-900">{profile?.fullName || 'Not available yet'}</p>
            </div>
            <div className="rounded-xl border border-slate-200 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">Email</p>
              <p className="mt-2 text-sm font-semibold text-slate-900">{profile?.email || 'Not available yet'}</p>
            </div>
            <div className="rounded-xl border border-slate-200 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">Course</p>
              <p className="mt-2 text-sm font-semibold text-slate-900">{profile?.courseName || 'Assigned after approval'}</p>
            </div>
            <div className="rounded-xl border border-slate-200 p-4">
              <p className="text-xs uppercase tracking-[0.2em] text-slate-500">Certificate ID</p>
              <p className="mt-2 text-sm font-semibold text-slate-900">{profile?.certificateId || 'Pending'}</p>
            </div>
          </div>
        </div>

        <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
          <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Learning modules</h3>
          <ul className="mt-4 space-y-3 text-sm">
            {learningModules.map((module) => (
              <li key={module.name} className="rounded-xl border border-slate-200 p-4">
                <p className="font-semibold text-slate-900">{module.name}</p>
                <div className="mt-2 flex items-center justify-between text-slate-600">
                  <span>{module.progress}</span>
                  <span className="rounded-full bg-cyan-50 px-2.5 py-1 text-xs font-semibold text-cyan-700">{module.state}</span>
                </div>
              </li>
            ))}
            {learningModules.length === 0 && !loading ? (
              <li className="rounded-xl border border-slate-200 p-4 text-slate-500">No module progress records available.</li>
            ) : null}
          </ul>
        </div>
      </div>

      <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
        <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Certification timeline</h2>
        <div className="mt-5 grid gap-3 md:grid-cols-4">
          {timeline.map((item, index) => (
            <div key={item} className="rounded-xl border border-slate-200 p-4 text-sm text-slate-700">
              <p className="mb-2 inline-flex h-6 w-6 items-center justify-center rounded-full bg-slate-900 text-xs font-semibold text-white">{index + 1}</p>
              <p>{item}</p>
            </div>
          ))}
          {timeline.length === 0 && !loading ? (
            <div className="rounded-xl border border-slate-200 p-4 text-sm text-slate-500 md:col-span-4">No timeline events available.</div>
          ) : null}
        </div>
      </div>
    </AppShell>
  )
}
