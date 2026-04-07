import { useEffect, useMemo, useState } from 'react'
import AppShell from '../components/AppShell'
import heroPartner from '../assets/hero-partner.svg'
import {
  createBatch,
  createEnrollment,
  deleteBatch,
  deleteEnrollment,
  getPartnerDashboard,
  submitBatch,
  updateEnrollment,
} from '../api/partnerApi'

export default function PartnerDashboard() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [actionMessage, setActionMessage] = useState('')
  const [workingKey, setWorkingKey] = useState('')
  const [batchForm, setBatchForm] = useState({ batchCode: '', studentCount: 12 })
  const [enrollmentForm, setEnrollmentForm] = useState({ fullName: '', email: '', courseName: '', completionStatus: 'Active', batchId: '' })
  const [editingEnrollmentId, setEditingEnrollmentId] = useState(null)

  const loadDashboard = async (showLoading = false) => {
    if (showLoading) {
      setLoading(true)
    }

    try {
      const response = await getPartnerDashboard()
      setData(response)
      setError('')
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to load partner dashboard data.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadDashboard(true)
  }, [])

  const runAction = async (key, action, successMessage) => {
    setWorkingKey(key)
    setActionMessage('')
    setError('')

    try {
      await action()
      setActionMessage(successMessage)
      await loadDashboard()
    } catch (err) {
      setError(err.response?.data?.message || 'Action failed. Please try again.')
    } finally {
      setWorkingKey('')
    }
  }

  const confirmAndRun = (message, key, action, successMessage) => {
    if (!window.confirm(message)) {
      return
    }

    runAction(key, action, successMessage)
  }

  const handleBatchCreate = async (event) => {
    event.preventDefault()
    await runAction(
      'create-batch',
      async () => {
        await createBatch({ batchCode: batchForm.batchCode, studentCount: Number(batchForm.studentCount) })
        setBatchForm({ batchCode: '', studentCount: 12 })
      },
      'New batch created.'
    )
  }

  const handleEnrollmentSubmit = async (event) => {
    event.preventDefault()

    const payload = {
      ...enrollmentForm,
      batchId: Number(enrollmentForm.batchId),
    }

    if (editingEnrollmentId) {
      await runAction(
        `update-enrollment-${editingEnrollmentId}`,
        async () => {
          await updateEnrollment(editingEnrollmentId, payload)
          setEditingEnrollmentId(null)
          setEnrollmentForm({ fullName: '', email: '', courseName: '', completionStatus: 'Active', batchId: '' })
        },
        'Learner record updated.'
      )
      return
    }

    await runAction(
      'create-enrollment',
      async () => {
        await createEnrollment(payload)
        setEnrollmentForm({ fullName: '', email: '', courseName: '', completionStatus: 'Active', batchId: '' })
      },
      'Learner enrolled and sent for review.'
    )
  }

  const stats = useMemo(() => {
    if (data?.stats?.length) {
      return data.stats
    }

    return []
  }, [data, loading])

  const batchPipeline = data?.batchPipeline || []
  const recentEnrollments = data?.recentEnrollments || []
  const checklist = data?.checklist || []
  const quickActions = data?.quickActions || []
  const highlights = data?.highlights || []

  return (
    <AppShell
      role="PARTNER"
      title="Partner Institute Workspace"
      subtitle="Create batches, enrol students, track approvals, and request DIGIQUAL learning modules without leaving the dashboard."
      accentClass="from-[#1d4ed8] via-[#0f766e] to-[#0284c7]"
      heroImage={heroPartner}
      heroBadge="Batch Pipeline"
      stats={stats}
      quickLinks={quickActions}
      highlights={highlights}
    >
      {error ? <div className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</div> : null}
      {actionMessage ? <div className="rounded-2xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">{actionMessage}</div> : null}

      <div className="grid gap-6 xl:grid-cols-[1.1fr_0.9fr]">
        <div className="space-y-6">
          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <div className="mb-4 flex items-center justify-between gap-3">
              <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Batch pipeline</h2>
              <span className="rounded-full bg-cyan-50 px-3 py-1 text-xs font-semibold text-cyan-700">Realtime</span>
            </div>

            <div className="overflow-hidden rounded-2xl border border-slate-200">
              <table className="min-w-full text-left text-sm">
                <thead className="bg-slate-100 text-slate-600">
                  <tr>
                    <th className="px-4 py-3 font-medium">Batch</th>
                    <th className="px-4 py-3 font-medium">Students</th>
                    <th className="px-4 py-3 font-medium">Status</th>
                    <th className="px-4 py-3 font-medium">Updated</th>
                    <th className="px-4 py-3 font-medium">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {batchPipeline.map((batch) => (
                    <tr key={batch.id ?? batch.batch} className="border-t border-slate-100 bg-white">
                      <td className="px-4 py-3 font-semibold text-slate-900">{batch.batch}</td>
                      <td className="px-4 py-3 text-slate-700">{batch.students}</td>
                      <td className="px-4 py-3">
                        <div className="space-y-2">
                          <span className="rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-700">{batch.status}</span>
                          {batch.reviewNote ? <p className="max-w-xs text-xs text-amber-700">Admin note: {batch.reviewNote}</p> : null}
                        </div>
                      </td>
                      <td className="px-4 py-3 text-slate-700">{batch.lastUpdate}</td>
                      <td className="px-4 py-3">
                        <div className="flex gap-2">
                          <button
                            type="button"
                            disabled={workingKey === `submit-batch-${batch.id}` || batch.status === 'SUBMITTED' || batch.status === 'APPROVED'}
                            onClick={() => confirmAndRun(`Submit ${batch.batch} for admin review?`, `submit-batch-${batch.id}`, () => submitBatch(batch.id), `${batch.batch} submitted for admin review.`)}
                            className="rounded-lg bg-slate-900 px-3 py-1.5 text-xs font-semibold text-white disabled:opacity-60"
                          >
                            Submit
                          </button>
                          <button
                            type="button"
                            disabled={workingKey === `delete-batch-${batch.id}`}
                            onClick={() => confirmAndRun(`Delete ${batch.batch}? This cannot be undone.`, `delete-batch-${batch.id}`, () => deleteBatch(batch.id), `${batch.batch} deleted.`)}
                            className="rounded-lg border border-rose-200 px-3 py-1.5 text-xs font-semibold text-rose-700 disabled:opacity-60"
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {batchPipeline.length === 0 && !loading ? (
                    <tr className="border-t border-slate-100 bg-white">
                      <td colSpan={5} className="px-4 py-4 text-center text-slate-500">No batches available.</td>
                    </tr>
                  ) : null}
                </tbody>
              </table>
            </div>
          </div>

          <div className="grid gap-6 lg:grid-cols-2">
            <form onSubmit={handleBatchCreate} className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
              <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Create batch</h3>
              <div className="mt-4 space-y-3">
                <input
                  value={batchForm.batchCode}
                  onChange={(event) => setBatchForm((current) => ({ ...current, batchCode: event.target.value }))}
                  placeholder="Batch code"
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                />
                <input
                  type="number"
                  min="1"
                  value={batchForm.studentCount}
                  onChange={(event) => setBatchForm((current) => ({ ...current, studentCount: event.target.value }))}
                  placeholder="Student count"
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                />
                <button type="submit" disabled={workingKey === 'create-batch'} className="w-full rounded-xl bg-cyan-600 px-4 py-3 text-sm font-semibold text-white disabled:opacity-60">
                  Create batch
                </button>
              </div>
            </form>

            <form onSubmit={handleEnrollmentSubmit} className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
              <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">{editingEnrollmentId ? 'Edit learner' : 'Add learner'}</h3>
              <div className="mt-4 space-y-3">
                <input
                  value={enrollmentForm.fullName}
                  onChange={(event) => setEnrollmentForm((current) => ({ ...current, fullName: event.target.value }))}
                  placeholder="Full name"
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                />
                <input
                  value={enrollmentForm.email}
                  onChange={(event) => setEnrollmentForm((current) => ({ ...current, email: event.target.value }))}
                  placeholder="Email"
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                />
                <input
                  value={enrollmentForm.courseName}
                  onChange={(event) => setEnrollmentForm((current) => ({ ...current, courseName: event.target.value }))}
                  placeholder="Course name"
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                />
                <select
                  value={enrollmentForm.completionStatus}
                  onChange={(event) => setEnrollmentForm((current) => ({ ...current, completionStatus: event.target.value }))}
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                >
                  <option value="Active">Active</option>
                  <option value="Completed">Completed</option>
                  <option value="Action required">Action required</option>
                </select>
                <select
                  value={enrollmentForm.batchId}
                  onChange={(event) => setEnrollmentForm((current) => ({ ...current, batchId: event.target.value }))}
                  className="w-full rounded-xl border border-slate-300 px-4 py-3 text-sm outline-none"
                >
                  <option value="">Select batch</option>
                  {batchPipeline.map((batch) => (
                    <option key={batch.id ?? batch.batch} value={batch.id}>{batch.batch}</option>
                  ))}
                </select>
                <div className="flex gap-2">
                  <button type="submit" disabled={workingKey === 'create-enrollment' || (editingEnrollmentId && workingKey === `update-enrollment-${editingEnrollmentId}`)} className="flex-1 rounded-xl bg-slate-900 px-4 py-3 text-sm font-semibold text-white disabled:opacity-60">
                    {editingEnrollmentId ? 'Save learner' : 'Add learner'}
                  </button>
                  {editingEnrollmentId ? (
                    <button
                      type="button"
                      onClick={() => {
                        setEditingEnrollmentId(null)
                        setEnrollmentForm({ fullName: '', email: '', courseName: '', completionStatus: 'Active', batchId: '' })
                      }}
                      className="rounded-xl border border-slate-300 px-4 py-3 text-sm font-semibold text-slate-700"
                    >
                      Cancel
                    </button>
                  ) : null}
                </div>
              </div>
            </form>
          </div>
        </div>

        <div className="space-y-6">
          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Recent enrolments</h3>
            <ul className="mt-4 space-y-3 text-sm">
              {recentEnrollments.map((student) => (
                <li key={student.id ?? `${student.name}-${student.course}`} className="rounded-xl border border-slate-200 p-4">
                  <p className="font-semibold text-slate-900">{student.name}</p>
                  <p className="mt-1 text-slate-600">{student.course}</p>
                  <p className="mt-2 inline-flex rounded-full bg-indigo-50 px-2.5 py-1 text-xs font-semibold text-indigo-700">{student.state}</p>
                  <div className="mt-3 flex gap-2">
                    <button
                      type="button"
                      onClick={() => {
                        setEditingEnrollmentId(student.id)
                        setEnrollmentForm({
                          fullName: student.name,
                          email: student.email,
                          courseName: student.course,
                          completionStatus: student.state === 'Approved' ? 'Completed' : 'Active',
                          batchId: student.batchId ? String(student.batchId) : '',
                        })
                      }}
                      className="rounded-lg bg-slate-100 px-3 py-1.5 text-xs font-semibold text-slate-700"
                    >
                      Edit
                    </button>
                    <button
                      type="button"
                      disabled={workingKey === `delete-enrollment-${student.id}`}
                      onClick={() => confirmAndRun(`Remove ${student.name} from this batch?`, `delete-enrollment-${student.id}`, () => deleteEnrollment(student.id), `${student.name} removed.`)}
                      className="rounded-lg border border-rose-200 px-3 py-1.5 text-xs font-semibold text-rose-700 disabled:opacity-60"
                    >
                      Delete
                    </button>
                  </div>
                </li>
              ))}
              {recentEnrollments.length === 0 && !loading ? <li className="rounded-xl border border-slate-200 p-4 text-slate-500">No recent enrolments found.</li> : null}
            </ul>
          </div>

          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Submission checklist</h3>
            <div className="mt-4 space-y-3 text-sm text-slate-700">
              {checklist.map((item) => (
                <div key={item} className="rounded-xl border border-slate-200 px-4 py-3">{item}</div>
              ))}
              {checklist.length === 0 && !loading ? <div className="rounded-xl border border-slate-200 px-4 py-3 text-slate-500">No checklist items available.</div> : null}
            </div>
          </div>
        </div>
      </div>
    </AppShell>
  )
}
