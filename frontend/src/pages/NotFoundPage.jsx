import { ArrowLeft, Compass } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-[var(--canvas)] px-4 py-10">
      <div className="w-full max-w-xl rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-10 text-center shadow-[var(--shadow-soft)] backdrop-blur">
        <div className="mx-auto mb-6 flex h-16 w-16 items-center justify-center rounded-3xl bg-[var(--panel-muted)] text-slate-900">
          <Compass className="h-8 w-8" />
        </div>
        <p className="text-sm font-semibold uppercase tracking-[0.3em] text-slate-500">404</p>
        <h1 className="mt-3 font-display text-4xl font-semibold tracking-tight text-slate-950">Page not found</h1>
        <p className="mt-4 text-sm leading-7 text-slate-600">
          The route exists outside the current MVP. Use the main login page to enter the correct DIGIQUAL portal.
        </p>
        <Link
          to="/"
          className="mt-8 inline-flex items-center gap-2 rounded-2xl bg-slate-950 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800"
        >
          <ArrowLeft className="h-4 w-4" />
          Return to login
        </Link>
      </div>
    </div>
  )
}