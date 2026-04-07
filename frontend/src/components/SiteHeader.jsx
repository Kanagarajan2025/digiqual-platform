import { Link } from 'react-router-dom'

export default function SiteHeader() {
  const linkClass = 'text-sm font-medium text-slate-700 hover:text-orange-600'

  return (
    <header className="sticky top-0 z-30 border-b border-slate-200 bg-white/90 backdrop-blur">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3 md:px-6">
        <Link to="/home" className="font-display text-xl font-semibold text-slate-900">DIGIQUAL</Link>
        <nav className="hidden items-center gap-5 md:flex">
          <Link to="/home" className={linkClass}>Home</Link>
          <Link to="/about" className={linkClass}>About</Link>
          <Link to="/courses" className={linkClass}>Courses</Link>
          <Link to="/contact" className={linkClass}>Contact</Link>
          <Link to="/certificate-verification" className={linkClass}>Certificate Verification</Link>
        </nav>
        <div className="flex items-center gap-2">
          <Link to="/admin-login" className="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-semibold text-slate-700 hover:border-orange-400 hover:text-orange-600">Admin Login</Link>
          <Link to="/partner-login" className="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-semibold text-slate-700 hover:border-orange-400 hover:text-orange-600">Partner Login</Link>
          <Link to="/student-login" className="rounded-lg bg-orange-600 px-3 py-1.5 text-xs font-semibold text-white hover:bg-orange-700">Student Login</Link>
        </div>
      </div>
    </header>
  )
}
